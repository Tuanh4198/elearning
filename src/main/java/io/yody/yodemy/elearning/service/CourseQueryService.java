package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.CourseEntity;
import io.yody.yodemy.elearning.domain.CourseEntity_;
import io.yody.yodemy.elearning.domain.enumeration.CourseStatusEnum;
import io.yody.yodemy.elearning.repository.CourseRepository;
import io.yody.yodemy.elearning.service.criteria.CourseCriteria;
import io.yody.yodemy.elearning.service.dto.CourseDTO;
import io.yody.yodemy.elearning.service.helpers.CourseHelper;
import io.yody.yodemy.elearning.service.mapper.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CourseQueryService extends QueryService<CourseEntity> {

    private static final String ENTITY_NAME = "course";
    private final Logger log = LoggerFactory.getLogger(ExamQueryService.class);
    private final CourseRepository courseRepository;
    private final CourseHelper courseHelper;

    public CourseQueryService(CourseRepository courseRepository, CourseHelper courseHelper) {
        this.courseRepository = courseRepository;
        this.courseHelper = courseHelper;
    }

    private void enrich(List<CourseDTO> courseDTOS) {
        courseHelper.enrichDocument(courseDTOS);
        courseHelper.enrichCategory(courseDTOS);
        courseHelper.enrichMetafields(courseDTOS);
    }

    @Transactional(readOnly = true)
    public Page<CourseDTO> findByCriteria(CourseCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Exams");
        final Specification<CourseEntity> specification = createSpecification(criteria);

        Page<CourseEntity> examEntities = courseRepository.findAll(specification, pageable);
        List<CourseEntity> entityList = examEntities.getContent();
        List<CourseDTO> dtoList = CourseMapper.INSTANCE.toDto(entityList);
        enrich(dtoList);
        return new PageImpl<>(dtoList, pageable, examEntities.getTotalElements());
    }

    /**
     * Get one exam by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public CourseDTO findOne(Long id) {
        log.debug("Request to get Exam : {}", id);
        Optional<CourseDTO> courseDTOOptional = courseRepository.findById(id).map(CourseMapper.INSTANCE::toDto);
        if (!courseDTOOptional.isPresent()) {
            return null;
        }
        CourseDTO courseDTO = courseDTOOptional.get();
        enrich(List.of(courseDTO));
        return courseDTO;
    }

    protected Specification<CourseEntity> createSpecification(CourseCriteria criteria) {
        Specification<CourseEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CourseEntity_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), CourseEntity_.title));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCategoryId(), CourseEntity_.categoryId));
            }
            if (
                !Objects.isNull(criteria.getStatus()) &&
                Objects.equals(criteria.getStatus().toLowerCase(), CourseStatusEnum.IN_PROGRESS.name().toLowerCase())
            ) {
                Instant now = Instant.now();
                specification =
                    specification.and((root, query, cb) ->
                        cb.and(
                            cb.greaterThanOrEqualTo(root.get(CourseEntity_.expireTime), now),
                            cb.lessThanOrEqualTo(root.get(CourseEntity_.applyTime), now)
                        )
                    );
            }
            specification = specification.and((root, query, cb) -> cb.and(cb.equal(root.get(CourseEntity_.deleted), root.get("deleted"))));
        }
        return specification;
    }
}
