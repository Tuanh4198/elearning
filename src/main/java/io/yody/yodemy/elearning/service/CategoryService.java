package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.CategoryEntity;
import io.yody.yodemy.elearning.domain.enumeration.CategoryTypeEnum;
import io.yody.yodemy.elearning.repository.CategoryRepository;
import io.yody.yodemy.elearning.service.dto.CategoryDTO;
import io.yody.yodemy.elearning.service.mapper.CategoryMapper;
import org.nentangso.core.service.errors.NtsValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CategoryEntity}.
 */
@Service
@Transactional
public class CategoryService {

    private static final String ENTITY_NAME = "category";
    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    private void validateCategory(CategoryDTO categoryDTO) {
        String title = categoryDTO.getTitle();
        Long id = categoryDTO.getId();
        CategoryTypeEnum type = categoryDTO.getType();

        boolean titleExists;
        if (id == null) {
            titleExists = categoryRepository.existsByTitleAndType(title, type);
        } else {
            titleExists = categoryRepository.existsByTitleAndTypeAndIdNot(title, type, id);
        }

        if (titleExists) {
            throw new NtsValidationException("message", String.format("Tên danh mục %s đã tồn tại", title));
        }
    }

    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryDTO save(CategoryDTO categoryDTO) {
        log.debug("Request to save Category : {}", categoryDTO);
//        validateCategory(categoryDTO);
        CategoryEntity categoryEntity = categoryMapper.toEntity(categoryDTO);
        categoryEntity = categoryRepository.save(categoryEntity);
        return categoryMapper.toDto(categoryEntity);
    }

    /**
     * Update a category.
     *
     * @param categoryDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryDTO update(CategoryDTO categoryDTO) {
        log.debug("Request to update Category : {}", categoryDTO);
        CategoryEntity categoryEntity = categoryMapper.toEntity(categoryDTO);
        categoryEntity = categoryRepository.save(categoryEntity);
        return categoryMapper.toDto(categoryEntity);
    }

    /**
     * Partially update a category.
     *
     * @param categoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryDTO> partialUpdate(CategoryDTO categoryDTO) {
        log.debug("Request to partially update Category : {}", categoryDTO);

        return categoryRepository
            .findById(categoryDTO.getId())
            .map(existingCategory -> {
                categoryMapper.partialUpdate(existingCategory, categoryDTO);

                return existingCategory;
            })
            .map(categoryRepository::save)
            .map(categoryMapper::toDto);
    }

    /**
     * Delete the category by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.deleteById(id);
    }
}
