package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.AttachmentEntity;
import io.yody.yodemy.elearning.domain.AttachmentEntity_;
import io.yody.yodemy.elearning.repository.AttachmentRepository;
import io.yody.yodemy.elearning.service.criteria.AttachmentCriteria;
import io.yody.yodemy.elearning.service.dto.AttachmentDTO;
import io.yody.yodemy.elearning.service.helpers.ThumbHelper;
import io.yody.yodemy.elearning.service.mapper.AttachmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AttachmentQueryService extends QueryService<AttachmentEntity> {

    private final Logger log = LoggerFactory.getLogger(AttachmentQueryService.class);

    private final AttachmentRepository attachmentRepository;

    private final AttachmentMapper attachmentMapper;
    private final ThumbHelper thumbHelper;

    public AttachmentQueryService(AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper, ThumbHelper thumbHelper) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
        this.thumbHelper = thumbHelper;
    }

    /**
     * Return a {@link List} of {@link AttachmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AttachmentDTO> findByCriteria(AttachmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AttachmentEntity> specification = createSpecification(criteria);
        return attachmentMapper.toDto(attachmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AttachmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AttachmentDTO> findByCriteria(AttachmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AttachmentEntity> specification = createSpecification(criteria);

        Page<AttachmentDTO> dtoPage = attachmentRepository.findAll(specification, page).map(attachmentMapper::toDto);
        thumbHelper.enrichThumb(dtoPage.getContent(), AttachmentDTO::getUrl, AttachmentDTO::setThumbUrl);

        return dtoPage;
    }

    /**
     * Function to convert {@link AttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AttachmentEntity> createSpecification(AttachmentCriteria criteria) {
        Specification<AttachmentEntity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AttachmentEntity_.id));
            }
            if (criteria.getSource() != null) {
                specification = specification.and(buildSpecification(criteria.getSource(), AttachmentEntity_.source));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), AttachmentEntity_.url));
            }
            if (criteria.getAttachmentType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAttachmentType(), AttachmentEntity_.attachmentType));
            }
            if (criteria.getAttachmentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAttachmentName(), AttachmentEntity_.attachmentName));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), AttachmentEntity_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), AttachmentEntity_.createdAt));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUpdatedBy(), AttachmentEntity_.updatedBy));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), AttachmentEntity_.updatedAt));
            }
        }
        return specification;
    }
}
