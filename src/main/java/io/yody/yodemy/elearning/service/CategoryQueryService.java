package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.CategoryEntity;
import io.yody.yodemy.elearning.repository.CategoryRepository;
import io.yody.yodemy.elearning.service.criteria.CategoryCriteria;
import io.yody.yodemy.elearning.service.criteria.ExamCriteria;
import io.yody.yodemy.elearning.service.dto.CategoryDTO;
import io.yody.yodemy.elearning.service.mapper.CategoryMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
public class CategoryQueryService extends QueryService<CategoryEntity> {

    private static final String ENTITY_NAME = "category";
    private final Logger log = LoggerFactory.getLogger(CategoryQueryService.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryQueryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Get all the categories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(CategoryCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Categories");
        Specification specification = createSpecification(criteria);
        Page<CategoryEntity> page = categoryRepository.findAll(specification, pageable);
        List<CategoryEntity> categoryEntities = page.getContent();
        List<CategoryDTO> categoryDTOS = CategoryMapper.INSTANCE.toDto(categoryEntities);
        return new PageImpl<>(categoryDTOS, pageable, page.getTotalElements());
    }

    /**
     * Get one category by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryDTO> findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        return categoryRepository.findById(id).map(categoryMapper::toDto);
    }

    protected Specification<CategoryEntity> createSpecification(CategoryCriteria criteria) {
        Specification<CategoryEntity> specification = new Specification<CategoryEntity>() {
            @Override
            public Predicate toPredicate(Root<CategoryEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (criteria.getType() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), criteria.getType()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        return specification;
    }
}
