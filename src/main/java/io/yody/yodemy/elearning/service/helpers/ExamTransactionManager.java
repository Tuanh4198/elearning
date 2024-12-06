package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.domain.MetafieldEntity;
import io.yody.yodemy.elearning.domain.DocumentEntity;
import io.yody.yodemy.elearning.domain.ExamEntity;
import io.yody.yodemy.elearning.domain.ExamQuizzPoolEntity;
import io.yody.yodemy.elearning.domain.constant.NextIdConst;
import io.yody.yodemy.elearning.repository.DocumentRepository;
import io.yody.yodemy.elearning.repository.ExamQuizzPoolRepository;
import io.yody.yodemy.elearning.repository.ExamRepository;
import io.yody.yodemy.elearning.repository.MetafieldRepository;
import io.yody.yodemy.elearning.service.business.*;
import io.yody.yodemy.elearning.service.business.redis.IDGenerator;
import io.yody.yodemy.elearning.service.dto.DocumentDTO;
import io.yody.yodemy.elearning.service.dto.ExamDTO;
import io.yody.yodemy.elearning.service.dto.ExamQuizzPoolDTO;
import io.yody.yodemy.elearning.service.mapper.DocumentMapper;
import io.yody.yodemy.elearning.service.mapper.ExamMapper;
import io.yody.yodemy.elearning.service.mapper.ExamQuizzPoolMapper;
import io.yody.yodemy.elearning.service.mapper.MetafieldMapper;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleNamespace;
import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;
import io.yody.yodemy.rule.knowledgeBase.mapper.RuleMapper;
import io.yody.yodemy.rule.knowledgeBase.repository.RuleRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.QUIZZ_POOL_OWNER_RESOURCE;

@Component
public class ExamTransactionManager {

    private static final Logger log = LoggerFactory.getLogger(ExamTransactionManager.class);
    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final MetafieldRepository metafieldRepository;
    private final MetafieldMapper metafieldMapper;
    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final ExamQuizzPoolRepository examQuizzPoolRepository;
    private final ExamQuizzPoolMapper examQuizzPoolMapper;
    private final RuleRepository ruleRepository;
    private final IDGenerator idGenerator;

    public ExamTransactionManager(
        ExamRepository examRepository,
        ExamMapper examMapper,
        MetafieldRepository metafieldRepository,
        MetafieldMapper metafieldMapper,
        ExamQuizzPoolRepository examQuizzPoolRepository,
        ExamQuizzPoolMapper examQuizzPoolMapper,
        DocumentMapper documentMapper,
        DocumentRepository documentRepository,
        RuleRepository ruleRepository,
        IDGenerator idGenerator
    ) {
        this.examRepository = examRepository;
        this.examMapper = examMapper;
        this.metafieldRepository = metafieldRepository;
        this.metafieldMapper = metafieldMapper;
        this.examQuizzPoolRepository = examQuizzPoolRepository;
        this.examQuizzPoolMapper = examQuizzPoolMapper;
        this.documentMapper = documentMapper;
        this.documentRepository = documentRepository;
        this.ruleRepository = ruleRepository;
        this.idGenerator = idGenerator;
    }

    private List<ExamQuizzPoolDTO> saveQuizzPoolEntities(ExamBO examBO, Long examId) {
        List<ExamQuizzPoolBO> examQuizzPoolBOS = examBO.getQuizzPools();
        List<ExamQuizzPoolEntity> examQuizzPoolEntities = examQuizzPoolMapper.bosToEntities(examQuizzPoolBOS);
        for (ExamQuizzPoolEntity examQuizzPoolEntity : examQuizzPoolEntities) {
            examQuizzPoolEntity.setRootId(examId);
        }
        examQuizzPoolEntities = examQuizzPoolRepository.saveAll(examQuizzPoolEntities);
        List<ExamQuizzPoolDTO> examQuizzPoolDTOS = examQuizzPoolMapper.entitiesToDtos(examQuizzPoolEntities);

        Map<Long, Long> idToQuizzPoolEntityMap = examQuizzPoolEntities
            .stream()
            .collect(Collectors.toMap(ExamQuizzPoolEntity::getSourceId, examEntity -> examEntity.getId()));
        List<MetafieldEntity> metafieldEntities = examQuizzPoolBOS
            .stream()
            .flatMap(quizzPoolBO ->
                quizzPoolBO
                    .getMetafields()
                    .stream()
                    .map(metafieldBO -> {
                        MetafieldEntity metafieldEntity = metafieldMapper.boToEntity(metafieldBO);
                        metafieldEntity.setOwnerId(idToQuizzPoolEntityMap.get(quizzPoolBO.getSourceId()));
                        metafieldEntity.setOwnerResource(QUIZZ_POOL_OWNER_RESOURCE);
                        metafieldEntity.setNamespace(QUIZZ_POOL_OWNER_RESOURCE);
                        metafieldEntity.setType(QUIZZ_POOL_OWNER_RESOURCE);
                        return metafieldEntity;
                    })
            )
            .collect(Collectors.toList());
        metafieldRepository.saveAll(metafieldEntities);

        return examQuizzPoolDTOS;
    }

    private List<DocumentDTO> saveDocuments(ExamBO exam, Long examId) {
        List<DocumentBO> documentBOS = exam.getDocuments();
        if (ObjectUtils.isEmpty(documentBOS)) {
            return Collections.<DocumentDTO>emptyList();
        }

        // Map BOs to entities
        List<DocumentEntity> documentEntities = documentMapper.bosToEntities(documentBOS);

        documentEntities = documentEntities.stream()
            .filter(document -> !ObjectUtils.isEmpty(document.getContent()))
            .collect(Collectors.toList());

        // Set the root ID for each document
        for (DocumentEntity document : documentEntities) {
            document.setRootId(examId);
        }

        // Save the document entities
        documentEntities = documentRepository.saveAll(documentEntities);

        return documentMapper.toDto(documentEntities);
    }

    private List<RuleDTO> saveRules(List<ExamRuleBO> ruleBOS, Long examId) {
        List<RuleEntity> rules = RuleMapper.INSTANCE.examBosToEntities(ruleBOS);
        for (RuleEntity rule : rules) {
            if (Objects.isNull(rule.getId())) {
                rule.setId(idGenerator.nextId(NextIdConst.RULE));
            }
            rule.setRootId(examId);
        }
        rules = ruleRepository.saveAll(rules);
        return RuleMapper.INSTANCE.toDtos(rules);
    }

    @Transactional
    public ExamDTO save(ExamBO examBO) {
        ExamEntity examEntity = examMapper.boToEntity(examBO);
        examEntity = examRepository.save(examEntity);
        if (Objects.isNull(examEntity.getId())) {
            log.error("Không lưu được bài thi %s", examEntity.getTitle());
            return null;
        }
        ExamDTO examDTO = examMapper.toDto(examEntity);

        List<DocumentDTO> documentDTOS = saveDocuments(examBO, examDTO.getId());
        examDTO.setDocuments(documentDTOS);

        List<ExamQuizzPoolDTO> quizzPoolDTOS = saveQuizzPoolEntities(examBO, examDTO.getId());
        examDTO.setQuizzPools(quizzPoolDTOS);

        List<RuleDTO> ruleDTOS = saveRules(examBO.getRules(), examDTO.getId());
        examDTO.setRules(ruleDTOS);

        return examDTO;
    }

    private void enrichQuizzPool(ExamBO examBO) {
        Long examId = examBO.getId();
        List<ExamQuizzPoolEntity> examQuizzPoolEntities = examQuizzPoolRepository.findAllByRootId(examId);
        List<ExamQuizzPoolBO> examQuizzPoolBOS = examQuizzPoolMapper.entitiesToBos(examQuizzPoolEntities);
        examBO.setQuizzPools(examQuizzPoolBOS);
    }

    private void enrichQuizzPoolMetafield(ExamBO examBO) {
        List<ExamQuizzPoolBO> quizzPoolBOS = examBO.getQuizzPools();
        List<Long> quizzPoolIds = quizzPoolBOS.stream().map(ExamQuizzPoolBO::getId).collect(Collectors.toList());
        List<MetafieldEntity> quizzPoolMetafieldEntities = metafieldRepository.findAllByOwnerResourceAndOwnerIdIn(
            QUIZZ_POOL_OWNER_RESOURCE,
            quizzPoolIds
        );
        List<MetafieldBO> quizzPoolMetafieldBOS = metafieldMapper.entitiesToBos(quizzPoolMetafieldEntities);
        Map<Long, List<MetafieldBO>> metafieldMap = quizzPoolMetafieldBOS.stream().collect(Collectors.groupingBy(MetafieldBO::getOwnerId));

        quizzPoolBOS.forEach(quizzPoolBO -> quizzPoolBO.setMetafields(metafieldMap.getOrDefault(quizzPoolBO.getId(), List.of())));
    }

    private void enrichDocuments(ExamBO examBO) {
        List<DocumentEntity> documents = documentRepository.findAllByRootId(examBO.getId());
        examBO.setDocuments(documentMapper.entitiesToBos(documents));
    }

    private void enrichRules(ExamBO examBO) {
        List<RuleEntity> rules = ruleRepository.findByNamespaceAndRootId(String.valueOf(RuleNamespace.EXAM), examBO.getId());
        List<ExamRuleBO> ruleBOS = RuleMapper.INSTANCE.examEntitiesToBos(rules);
        examBO.setRules(ruleBOS);
    }

    @Transactional(readOnly = true)
    public ExamBO findByExamId(Long examId) {
        ExamEntity examEntity = examRepository.findById(examId).filter(Predicate.not(ExamEntity::isDeleted)).orElse(null);
        if (examEntity == null) {
            log.error("Không tìm thấy bài kiểm tra");
            return null;
        }
        ExamBO examBO = examMapper.entityToBo(examEntity);
        enrichQuizzPool(examBO);
        enrichQuizzPoolMetafield(examBO);
        enrichDocuments(examBO);
        enrichRules(examBO);
        return examBO;
    }
}
