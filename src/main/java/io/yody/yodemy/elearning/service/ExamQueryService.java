package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.ExamEntity;
import io.yody.yodemy.elearning.domain.ExamEntity_;
import io.yody.yodemy.elearning.repository.ExamRepository;
import io.yody.yodemy.elearning.service.criteria.ExamCriteria;
import io.yody.yodemy.elearning.service.dto.ExamDTO;
import io.yody.yodemy.elearning.service.helpers.ExamHelper;
import io.yody.yodemy.elearning.service.mapper.ExamMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class ExamQueryService extends QueryService<ExamEntity> {

    private static final String ENTITY_NAME = "exam";
    private final Logger log = LoggerFactory.getLogger(ExamQueryService.class);
    private final ExamRepository examRepository;
    private final ExamHelper examHelper;

    public ExamQueryService(ExamRepository examRepository, ExamHelper examHelper) {
        this.examRepository = examRepository;
        this.examHelper = examHelper;
    }

    private void enrich(List<ExamDTO> examDTOS) {
        examHelper.enrichMetafields(examDTOS);
        examHelper.enrichQuizzPools(examDTOS);
        examHelper.enrichCategory(examDTOS);
    }

    @Transactional(readOnly = true)
    public Page<ExamDTO> findByCriteria(ExamCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Exams");
        final Specification<ExamEntity> specification = createSpecification(criteria);

        Page<ExamEntity> examEntities = examRepository.findAll(specification, pageable);
        List<ExamEntity> entityList = examEntities.getContent();
        List<ExamDTO> dtoList = ExamMapper.INSTANCE.toDto(entityList);
        enrich(dtoList);
        Page<ExamDTO> examDTOS = new PageImpl<>(dtoList, pageable, examEntities.getTotalElements());
        return examDTOS;
    }

    /**
     * Get one exam by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public ExamDTO findOne(Long id) {
        log.debug("Request to get Exam : {}", id);
        Optional<ExamDTO> examDTOOptional = examRepository.findById(id).map(ExamMapper.INSTANCE::toDto);
        if (!examDTOOptional.isPresent()) {
            return null;
        }
        ExamDTO examDTO = examDTOOptional.get();
        enrich(List.of(examDTO));
        return examDTO;
    }

    protected Specification<ExamEntity> createSpecification(ExamCriteria criteria) {
        Specification<ExamEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExamEntity_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), ExamEntity_.title));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCategoryId(), ExamEntity_.categoryId));
            }
        }
        return specification;
    }
}
