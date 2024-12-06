package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.domain.MetafieldEntity;
import io.yody.yodemy.elearning.domain.CategoryEntity;
import io.yody.yodemy.elearning.domain.DocumentEntity;
import io.yody.yodemy.elearning.repository.CategoryRepository;
import io.yody.yodemy.elearning.repository.DocumentRepository;
import io.yody.yodemy.elearning.repository.MetafieldRepository;
import io.yody.yodemy.elearning.service.dto.CategoryDTO;
import io.yody.yodemy.elearning.service.dto.CourseDTO;
import io.yody.yodemy.elearning.service.dto.DocumentDTO;
import io.yody.yodemy.elearning.service.dto.MetafieldDTO;
import io.yody.yodemy.elearning.service.mapper.CategoryMapper;
import io.yody.yodemy.elearning.service.mapper.DocumentMapper;
import io.yody.yodemy.elearning.service.mapper.MetafieldMapper;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;
import io.yody.yodemy.rule.knowledgeBase.mapper.RuleMapper;
import io.yody.yodemy.rule.knowledgeBase.repository.RuleRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.COURSE_OWNER_RESOURCE;
import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.DOCUMENT_RESOURCE;

@Component
public class CourseHelper {
    private final MetafieldRepository metafieldRepository;
    private final DocumentRepository documentRepository;
    private final CategoryRepository categoryRepository;
    private final RuleRepository ruleRepo;

    public CourseHelper(
        MetafieldRepository metafieldRepository,
        DocumentRepository documentRepository,
        CategoryRepository categoryRepository,
        RuleRepository ruleRepo
    ) {
        this.metafieldRepository = metafieldRepository;
        this.documentRepository = documentRepository;
        this.categoryRepository = categoryRepository;
        this.ruleRepo = ruleRepo;
    }

    public void enrichRules(List<CourseDTO> courses) {
        List<DocumentDTO> documents = courses.stream()
            .flatMap(course -> course.getDocuments().stream())
            .collect(Collectors.toList());

        List<Long> documentIds = documents.stream()
            .map(DocumentDTO::getId)
            .collect(Collectors.toList());

        List<RuleEntity> documentRules = ruleRepo.findByNamespaceAndRootIdIn("DOCUMENT", documentIds);
        List<RuleDTO> documentRuleDtos = documentRules.stream()
            .map(RuleMapper.INSTANCE::toDto)
            .collect(Collectors.toList());

        Map<Long, List<RuleDTO>> documentRuleDtoMap = documentRuleDtos.stream()
            .collect(Collectors.groupingBy(RuleDTO::getRootId));

        for (DocumentDTO document : documents) {
            List<RuleDTO> rulesForDocument = documentRuleDtoMap.getOrDefault(document.getId(), Collections.emptyList());
            document.setRules(rulesForDocument);
        }
    }

    public void enrichDocument(List<CourseDTO> courseDTOS) {
        List<Long> courseIds = courseDTOS.stream().map(CourseDTO::getId).collect(Collectors.toList());
        List<DocumentEntity> documentEntities = documentRepository.findAllByRootIdIn(courseIds);
        List<DocumentDTO> documentDTOS = DocumentMapper.INSTANCE.entitiesToDtos(documentEntities);
        //get all file
        enrichDocuments(documentDTOS);
        Map<Long, List<DocumentDTO>> documentMap = documentDTOS.stream().collect(Collectors.groupingBy(DocumentDTO::getRootId));
        for (CourseDTO courseDTO : courseDTOS) {
            Long id = courseDTO.getId();
            if (!Objects.isNull(id)) {
                List<DocumentDTO> documentDTOList = documentMap.get(id);
                courseDTO.setDocuments(documentDTOList);
            }
        }
        enrichRules(courseDTOS);
    }

    private void enrichDocuments(List<DocumentDTO> documentDTOS) {
        List<Long> documentIds = documentDTOS.stream().map(DocumentDTO::getId).collect(Collectors.toList());
        List<MetafieldEntity> metafieldEntities = metafieldRepository.findAllByOwnerResourceAndOwnerIdIn(DOCUMENT_RESOURCE, documentIds);
        List<MetafieldDTO> metafieldDTOS = MetafieldMapper.INSTANCE.entitiesToDtos(metafieldEntities);
        Map<Long, List<MetafieldDTO>> metafieldMap = metafieldDTOS.stream().collect(Collectors.groupingBy(MetafieldDTO::getOwnerId));
        for (DocumentDTO documentDTO : documentDTOS) {
            Long id = documentDTO.getId();
            if (!Objects.isNull(id)) {
                List<MetafieldDTO> metafieldDTOList = metafieldMap.get(id);
                documentDTO.setMetafields(metafieldDTOList);
            }
        }
    }

    public void enrichCategory(List<CourseDTO> courseDTOS) {
        List<Long> categoryIds = courseDTOS.stream().map(CourseDTO::getCategoryId).collect(Collectors.toList());
        List<CategoryEntity> categories = categoryRepository.findAllByIdIn(categoryIds);
        Map<Long, CategoryEntity> categoryMap = categories.stream().collect(Collectors.toMap(CategoryEntity::getId, Function.identity()));
        for (CourseDTO courseDTO : courseDTOS) {
            Long categoryId = courseDTO.getCategoryId();
            if (!Objects.isNull(categoryId)) {
                CategoryEntity category = categoryMap.get(categoryId);
                CategoryDTO categoryDTO = CategoryMapper.INSTANCE.toDto(category);
                courseDTO.setCategory(categoryDTO);
            }
        }
    }

    public void enrichMetafields(List<CourseDTO> courseDTOS) {
        List<Long> courseIds = courseDTOS.stream().map(CourseDTO::getId).collect(Collectors.toList());
        List<MetafieldEntity> metafields = metafieldRepository.findAllByOwnerResourceAndOwnerIdIn(COURSE_OWNER_RESOURCE, courseIds);
        List<MetafieldDTO> metafieldDTOS = MetafieldMapper.INSTANCE.toDto(metafields);
        Map<Long, List<MetafieldDTO>> metafieldDTOsMap = metafieldDTOS.stream().collect(Collectors.groupingBy(MetafieldDTO::getOwnerId));

        for (CourseDTO courseDTO : courseDTOS) {
            if (!Objects.isNull(courseDTO.getId())) {
                courseDTO.setMetafields(metafieldDTOsMap.get(courseDTO.getId()));
            }
        }
    }

    public void enrichCourseEmployees(List<CourseDTO> courseDTOS) {
        enrichDocument(courseDTOS);
        enrichCategory(courseDTOS);
        enrichMetafields(courseDTOS);
    }
}
