package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.QuizzCategoryEntity;
import io.yody.yodemy.elearning.repository.QuizzCategoryRepository;
import io.yody.yodemy.elearning.service.dto.QuizzCategoryDTO;
import io.yody.yodemy.elearning.service.mapper.QuizzCategoryMapper;
import io.yody.yodemy.elearning.service.specification.QuizzCategorySpecification;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzCategoryRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzSearchRequest;
import java.util.List;
import java.util.Optional;
import org.nentangso.core.service.errors.NtsValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuizzCategoryEntity}.
 */
@Service
@Transactional
public class QuizzCategoryService {

    private final Logger log = LoggerFactory.getLogger(QuizzCategoryService.class);

    private final QuizzCategoryRepository quizzCategoryRepository;

    private final QuizzCategoryMapper quizzCategoryMapper;

    public QuizzCategoryService(QuizzCategoryRepository quizzCategoryRepository, QuizzCategoryMapper quizzCategoryMapper) {
        this.quizzCategoryRepository = quizzCategoryRepository;
        this.quizzCategoryMapper = quizzCategoryMapper;
    }

    /**
     * Save a quizzCategory.
     *
     * @param quizzCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizzCategoryDTO save(QuizzCategoryRequest quizzCategoryRequest) {
        log.debug("Request to save QuizzCategory : {}", quizzCategoryRequest);
        validateRequest(quizzCategoryRequest);
        QuizzCategoryEntity quizzCategoryEntity = quizzCategoryMapper.requestToEntity(quizzCategoryRequest);
        quizzCategoryEntity = quizzCategoryRepository.save(quizzCategoryEntity);
        return quizzCategoryMapper.toDto(quizzCategoryEntity);
    }

    private void validateRequest(QuizzCategoryRequest quizzRequest) {
        ///validate if needed
        String title = quizzRequest.getTitle().trim();
        if (title.isEmpty()) {
            throw new NtsValidationException("message", String.format("Danh mục câu hỏi không hợp lệ", "title"));
        }
        if (quizzRequest.getId() != null) {
            List<QuizzCategoryEntity> cates = quizzCategoryRepository.findDifferentIdAndTitle(quizzRequest.getId(), title, false);
            if (!cates.isEmpty()) {
                throw new NtsValidationException("message", String.format("Danh mục câu hỏi đã tồn tại", "title"));
            }
        } else {
            List<QuizzCategoryEntity> cate = quizzCategoryRepository.findByTitle(title, false);
            if (!cate.isEmpty()) {
                throw new NtsValidationException("message", String.format("Danh mục câu hỏi đã tồn tại", "title"));
            }
        }
    }

    /**
     * Update a quizzCategory.
     *
     * @param quizzCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizzCategoryDTO update(QuizzCategoryRequest quizzCategoryRequest) {
        log.debug("Request to update QuizzCategory : {}", quizzCategoryRequest);
        validateRequest(quizzCategoryRequest);
        QuizzCategoryEntity quizzCategoryEntity = quizzCategoryMapper.requestToEntity(quizzCategoryRequest);
        quizzCategoryEntity = quizzCategoryRepository.save(quizzCategoryEntity);
        return quizzCategoryMapper.toDto(quizzCategoryEntity);
    }

    /**
     * Partially update a quizzCategory.
     *
     * @param quizzCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuizzCategoryDTO> partialUpdate(QuizzCategoryDTO quizzCategoryDTO) {
        log.debug("Request to partially update QuizzCategory : {}", quizzCategoryDTO);

        return quizzCategoryRepository
            .findById(quizzCategoryDTO.getId())
            .map(existingQuizzCategory -> {
                quizzCategoryMapper.partialUpdate(existingQuizzCategory, quizzCategoryDTO);

                return existingQuizzCategory;
            })
            .map(quizzCategoryRepository::save)
            .map(quizzCategoryMapper::toDto);
    }

    /**
     * Get all the quizzCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuizzCategoryDTO> findAll(QuizzSearchRequest request, Pageable pageable) {
        log.debug("Request to get all QuizzCategories");
        QuizzCategorySpecification specification = new QuizzCategorySpecification().search(request.getSearch());

        return quizzCategoryRepository.findAll(specification, pageable).map(quizzCategoryMapper::toDto);
    }

    /**
     * Get one quizzCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuizzCategoryDTO> findOne(Long id) {
        log.debug("Request to get QuizzCategory : {}", id);
        return quizzCategoryRepository.findById(id).map(quizzCategoryMapper::toDto);
    }

    /**
     * Delete the quizzCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuizzCategory : {}", id);
        quizzCategoryRepository.deleteById(id);
    }
}
