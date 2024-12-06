package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.elearning.domain.CourseEntity;
import io.yody.yodemy.elearning.domain.DocumentEntity;
import io.yody.yodemy.elearning.domain.constant.NextIdConst;
import io.yody.yodemy.elearning.repository.CourseRepository;
import io.yody.yodemy.elearning.repository.DocumentRepository;
import io.yody.yodemy.elearning.repository.MetafieldRepository;
import io.yody.yodemy.elearning.service.business.CourseBO;
import io.yody.yodemy.elearning.service.business.DocumentBO;
import io.yody.yodemy.elearning.service.business.redis.IDGenerator;
import io.yody.yodemy.elearning.service.dto.CourseDTO;
import io.yody.yodemy.elearning.service.dto.DocumentDTO;
import io.yody.yodemy.elearning.service.mapper.CourseMapper;
import io.yody.yodemy.elearning.service.mapper.DocumentMapper;
import io.yody.yodemy.elearning.service.mapper.MetafieldMapper;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import io.yody.yodemy.rule.knowledgeBase.mapper.RuleMapper;
import io.yody.yodemy.rule.knowledgeBase.repository.RuleRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.nentangso.core.service.errors.NtsValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class CourseTransactionManager {
    private static final Logger log = LoggerFactory.getLogger(QuizzTransactionManager.class);
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final RuleRepository ruleRepository;
    private final IDGenerator idGenerator;

    public CourseTransactionManager(
        CourseRepository courseRepository,
        CourseMapper courseMapper,
        MetafieldRepository metafieldRepository,
        MetafieldMapper metafieldMapper,
        DocumentMapper documentMapper,
        DocumentRepository documentRepository,
        RuleRepository ruleRepository,
        IDGenerator idGenerator,
        CourseHelper courseHelper
    ) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.documentMapper = documentMapper;
        this.documentRepository = documentRepository;
        this.ruleRepository = ruleRepository;
        this.idGenerator = idGenerator;
    }

    private List<DocumentDTO> saveDocuments(CourseBO course, Long courseId) {
        List<DocumentBO> documentBOS = course.getDocuments();
        if (ObjectUtils.isEmpty(documentBOS)) {
            return Collections.<DocumentDTO>emptyList();
        }

        // Map BOs to entities
        List<DocumentEntity> documentEntities = documentMapper.bosToEntities(documentBOS);

        // Set the root ID for each document
        for (DocumentEntity document : documentEntities) {
            document.setRootId(courseId);
        }

        // Save the document entities
        documentEntities = documentRepository.saveAll(documentEntities);

        // Prepare a map of content to document ID
        Map<String, Long> contentToDocumentIdMap = documentEntities.stream()
            .collect(Collectors.toMap(DocumentEntity::getContent, DocumentEntity::getId));

        // Extract and save rules
        List<RuleEntity> allRules = new ArrayList<>();
        for (DocumentBO documentBO : documentBOS) {
            List<RuleEntity> rules = RuleMapper.INSTANCE.documentBosToEntities(documentBO.getRules());
            Long documentId = contentToDocumentIdMap.get(documentBO.getContent());
            if (documentId != null) {
                for (RuleEntity rule : rules) {
                    if (Objects.isNull(rule.getId())) {
                        rule.setId(idGenerator.nextId(NextIdConst.RULE));
                    }
                    rule.setRootId(documentId);
                }
                allRules.addAll(rules);
            }
        }

        ruleRepository.saveAll(allRules);

        return documentMapper.toDto(documentEntities);
    }

    @Transactional(readOnly = true)
    public CourseBO findById(Long id) {
        CourseEntity entity = courseRepository.findById(id).filter(Predicate.not(CourseEntity::isDeleted)).orElse(null);
        if (entity == null) {
            throw new NtsValidationException("message", String.format("Không tìm thấy Node %s", id));
        }
        CourseBO bo = courseMapper.entityToBo(entity);
        return bo;
    }

    @Transactional
    public CourseDTO save(CourseBO courseBO) {
        CourseEntity courseEntity = courseMapper.boToEntity(courseBO);
        courseEntity = courseRepository.save(courseEntity);
        if (Objects.isNull(courseEntity.getId())) {
            log.error("Không lưu được khóa học %s", courseEntity.getTitle());
            return null;
        }
        List<DocumentDTO> documentDTOS = saveDocuments(courseBO, courseEntity.getId());
        CourseDTO courseDTO = courseMapper.toDto(courseEntity);
        courseDTO.setDocuments(documentDTOS);
        return courseDTO;
    }
}
