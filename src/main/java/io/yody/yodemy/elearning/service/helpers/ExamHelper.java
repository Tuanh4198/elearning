package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.domain.MetafieldEntity;
import io.yody.yodemy.elearning.domain.*;
import io.yody.yodemy.elearning.domain.enumeration.ExamQuizzPoolStrategyEnum;
import io.yody.yodemy.elearning.repository.*;
import io.yody.yodemy.elearning.service.dto.*;
import io.yody.yodemy.elearning.service.mapper.CategoryMapper;
import io.yody.yodemy.elearning.service.mapper.DocumentMapper;
import io.yody.yodemy.elearning.service.mapper.ExamQuizzPoolMapper;
import io.yody.yodemy.elearning.service.mapper.MetafieldMapper;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleNamespace;
import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;
import io.yody.yodemy.rule.knowledgeBase.mapper.RuleMapper;
import io.yody.yodemy.rule.knowledgeBase.repository.RuleRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.EXAM_OWNER_RESOURCE;
import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.QUIZZ_POOL_OWNER_RESOURCE;

@Component
public class ExamHelper {

    private final MetafieldRepository metafieldRepository;
    private final ExamQuizzPoolRepository examQuizzPoolRepository;
    private final CategoryRepository categoryRepository;
    private final DocumentRepository documentRepository;
    private final RuleRepository ruleRepository;
    private final QuizzRepository quizzRepository;
    private final QuizzCategoryRepository quizzCategoryRepository;
    private final ThumbHelper thumbHelper;

    public ExamHelper(
        MetafieldRepository metafieldRepository,
        ExamQuizzPoolRepository examQuizzPoolRepository,
        CategoryRepository categoryRepository,
        DocumentRepository documentRepository,
        RuleRepository ruleRepository,
        QuizzRepository quizzRepository,
        QuizzCategoryRepository quizzCategoryRepository,
        ThumbHelper thumbHelper
    ) {
        this.metafieldRepository = metafieldRepository;
        this.examQuizzPoolRepository = examQuizzPoolRepository;
        this.categoryRepository = categoryRepository;
        this.documentRepository = documentRepository;
        this.ruleRepository = ruleRepository;
        this.quizzRepository = quizzRepository;
        this.quizzCategoryRepository = quizzCategoryRepository;
        this.thumbHelper = thumbHelper;
    }

    public void enrichDocument(List<ExamDTO> examDTOS) {
        List<Long> examIds = examDTOS.stream().map(ExamDTO::getId).collect(Collectors.toList());
        List<DocumentEntity> documentEntities = documentRepository.findAllByRootIdIn(examIds);
        List<DocumentDTO> documentDTOS = DocumentMapper.INSTANCE.entitiesToDtos(documentEntities);
        thumbHelper.enrichThumb(documentDTOS, DocumentDTO::getContent, DocumentDTO::setErContent);

        //get all file
        Map<Long, List<DocumentDTO>> documentMap = documentDTOS.stream().collect(Collectors.groupingBy(DocumentDTO::getRootId));
        for (ExamDTO examDTO : examDTOS) {
            Long id = examDTO.getId();
            if (!Objects.isNull(id)) {
                List<DocumentDTO> documentDTOList = documentMap.get(id);
                examDTO.setDocuments(documentDTOList);
            }
        }
    }

    public void enrichCategory(List<ExamDTO> examDTOS) {
        List<Long> categoryIds = examDTOS.stream().map(ExamDTO::getCategoryId).collect(Collectors.toList());
        List<CategoryEntity> categories = categoryRepository.findAllByIdIn(categoryIds);
        Map<Long, CategoryEntity> categoryMap = categories.stream().collect(Collectors.toMap(CategoryEntity::getId, Function.identity()));
        for (ExamDTO examDTO : examDTOS) {
            Long categoryId = examDTO.getCategoryId();
            if (!Objects.isNull(categoryId)) {
                CategoryEntity category = categoryMap.get(categoryId);
                CategoryDTO categoryDTO = CategoryMapper.INSTANCE.toDto(category);
                examDTO.setCategory(categoryDTO);
            }
        }
    }

    public void enrichMetafields(List<ExamDTO> examDTOS) {
        List<Long> examIds = examDTOS.stream().map(ExamDTO::getId).collect(Collectors.toList());
        List<MetafieldEntity> metafields = metafieldRepository.findAllByOwnerResourceAndOwnerIdIn(EXAM_OWNER_RESOURCE, examIds);
        List<MetafieldDTO> metafieldDTOS = MetafieldMapper.INSTANCE.toDto(metafields);
        Map<Long, List<MetafieldDTO>> metafieldDTOsMap = metafieldDTOS.stream().collect(Collectors.groupingBy(MetafieldDTO::getOwnerId));

        for (ExamDTO examDTO : examDTOS) {
            if (!Objects.isNull(examDTO.getId())) {
                examDTO.setMetafields(metafieldDTOsMap.get(examDTO.getId()));
            }
        }
    }

    public void enrichQuizzPools(List<ExamDTO> examDTOS) {
        List<Long> examIds = examDTOS.stream().map(ExamDTO::getId).collect(Collectors.toList());
        List<ExamQuizzPoolEntity> quizzPools = examQuizzPoolRepository.findAllByRootIdIn(examIds);
        List<Long> quizzPoolIds = quizzPools.stream().map(ExamQuizzPoolEntity::getId).collect(Collectors.toList());
        List<MetafieldEntity> metafields = metafieldRepository.findAllByOwnerResourceAndOwnerIdIn(QUIZZ_POOL_OWNER_RESOURCE, quizzPoolIds);
        List<MetafieldDTO> metafieldDTOS = MetafieldMapper.INSTANCE.toDto(metafields);
        Map<Long, List<MetafieldDTO>> metafieldDTOsMap = metafieldDTOS.stream().collect(Collectors.groupingBy(MetafieldDTO::getOwnerId));

        Set<Long> quizzIds = new LinkedHashSet<>();
        Set<Long> quizzCategoryIds = new LinkedHashSet<>();

        List<ExamQuizzPoolDTO> quizzPoolDTOS = ExamQuizzPoolMapper.INSTANCE.entitiesToDtos(quizzPools);
        for (ExamQuizzPoolDTO quizzPoolDTO : quizzPoolDTOS) {
            List<MetafieldDTO> quizzPoolMetafieldDTOS = metafieldDTOsMap.get(quizzPoolDTO.getId());
            quizzPoolDTO.setMetafields(quizzPoolMetafieldDTOS);
        }

        Map<Long, List<ExamQuizzPoolDTO>> examQuizzPoolMap = quizzPoolDTOS
            .stream()
            .collect(Collectors.groupingBy(ExamQuizzPoolDTO::getRootId));
        for (ExamDTO examDTO : examDTOS) {
            if (!Objects.isNull(examDTO.getId())) {
                List<ExamQuizzPoolDTO> examQuizzPoolDTOS = examQuizzPoolMap.get(examDTO.getId());
                examDTO.setQuizzPools(examQuizzPoolDTOS);
                if (examDTO.getPoolStrategy().equals(ExamQuizzPoolStrategyEnum.MANUAL)) {
                    quizzIds.addAll(examDTO.getQuizzPools().stream().map(ExamQuizzPoolDTO::getSourceId).collect(Collectors.toList()));
                } else if (examDTO.getPoolStrategy().equals(ExamQuizzPoolStrategyEnum.WEIGHT)) {
                    quizzCategoryIds.addAll(
                        examDTO.getQuizzPools().stream().map(ExamQuizzPoolDTO::getSourceId).collect(Collectors.toList())
                    );
                }
            }
        }

        List<QuizzEntity> quizzEntities;
        Map<Long, QuizzEntity> quizzEntitiesMap = null;
        if (quizzIds.size() > 0) {
            quizzEntities = quizzRepository.findAllByIdIn(new ArrayList<>(quizzIds));
            quizzCategoryIds.addAll(quizzEntities.stream().map(QuizzEntity::getCategoryId).collect(Collectors.toList()));
            quizzEntitiesMap = quizzEntities.stream().collect(Collectors.toMap(QuizzEntity::getId, entity -> entity));
        }

        if (quizzCategoryIds.size() > 0) {
            List<QuizzCategoryEntity> quizzCategoryEntities = quizzCategoryRepository.findAllByIdIn(new ArrayList<>(quizzCategoryIds));
            Map<Long, QuizzCategoryEntity> quizzCategoryEntitiesMap = quizzCategoryEntities
                .stream()
                .collect(Collectors.toMap(QuizzCategoryEntity::getId, entity -> entity));
            for (ExamDTO examDTO : examDTOS) {
                if (!Objects.isNull(examDTO.getId()) && examDTO.getQuizzPools().size() > 0) {
                    List<ExamQuizzPoolDTO> examQuizzPoolDTOS = examDTO.getQuizzPools();
                    if (examDTO.getPoolStrategy().equals(ExamQuizzPoolStrategyEnum.MANUAL)) {
                        for (ExamQuizzPoolDTO quizzPoolDTO : examQuizzPoolDTOS) {
                            if (quizzEntitiesMap != null) {
                                QuizzEntity examQuizzEntity = quizzEntitiesMap.get(quizzPoolDTO.getSourceId());
                                QuizzCategoryEntity examQuizzCategoryEntity = quizzCategoryEntitiesMap.get(examQuizzEntity.getCategoryId());
                                quizzPoolDTO.setCategoryName(examQuizzCategoryEntity.getTitle());
                                quizzPoolDTO.setCategoryId(examQuizzCategoryEntity.getId());
                            }
                        }
                    } else if (examDTO.getPoolStrategy().equals(ExamQuizzPoolStrategyEnum.WEIGHT)) {
                        for (ExamQuizzPoolDTO quizzPoolDTO : examQuizzPoolDTOS) {
                            QuizzCategoryEntity examQuizzCategoryEntity = quizzCategoryEntitiesMap.get(quizzPoolDTO.getSourceId());
                            quizzPoolDTO.setCategoryName(examQuizzCategoryEntity.getTitle());
                            quizzPoolDTO.setCategoryId(examQuizzCategoryEntity.getId());
                        }
                    }
                }
            }
        }
    }

    public void enrichRules(List<ExamDTO> exams) {
        List<Long> examIds = exams.stream().map(ExamDTO::getId).collect(Collectors.toList());
        List<RuleEntity> rules = ruleRepository.findByNamespaceAndRootIdIn(String.valueOf(RuleNamespace.EXAM), examIds);
        List<RuleDTO> ruleDtos = rules.stream().map(RuleMapper.INSTANCE::toDto).collect(Collectors.toList());

        Map<Long, List<RuleDTO>> ruleDtoMap = ruleDtos.stream().collect(Collectors.groupingBy(RuleDTO::getRootId));

        for (ExamDTO exam : exams) {
            List<RuleDTO> ruleForExam = ruleDtoMap.getOrDefault(exam.getId(), Collections.emptyList());
            exam.setRules(ruleForExam);
        }
    }
}
