package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.QuizzCategoryEntity;
import io.yody.yodemy.elearning.domain.QuizzEntity;
import io.yody.yodemy.elearning.repository.MetafieldRepository;
import io.yody.yodemy.elearning.repository.QuizzAnswerRepository;
import io.yody.yodemy.elearning.repository.QuizzCategoryRepository;
import io.yody.yodemy.elearning.repository.QuizzRepository;
import io.yody.yodemy.elearning.service.business.MetafieldBO;
import io.yody.yodemy.elearning.service.business.QuizzAnswerBO;
import io.yody.yodemy.elearning.service.business.QuizzBO;
import io.yody.yodemy.elearning.service.dto.QuizzDTO;
import io.yody.yodemy.elearning.service.helpers.QuizzHelper;
import io.yody.yodemy.elearning.service.helpers.QuizzTransactionManager;
import io.yody.yodemy.elearning.service.mapper.QuizzAnswerMapper;
import io.yody.yodemy.elearning.service.mapper.QuizzMapper;
import io.yody.yodemy.elearning.service.specification.QuizzSpecification;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzAnswerRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzSearchRequest;
import org.nentangso.core.service.errors.NtsValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link QuizzEntity}.
 */
@Service
@Transactional
public class QuizzService {

    private static final String ENTITY_NAME = "quizz";
    private final Logger log = LoggerFactory.getLogger(QuizzService.class);
    private final QuizzRepository quizzRepository;
    private final QuizzAnswerRepository quizzAnswerRepository;
    private final QuizzCategoryRepository quizzCategoryRepository;

    private final QuizzMapper quizzMapper;
    private final QuizzAnswerMapper quizzAnswerMapper;
    private final MetafieldRepository metafieldRepository;

    private final QuizzTransactionManager quizzTransactionManager;
    private final QuizzHelper quizzHelper;

    public QuizzService(
        QuizzRepository quizzRepository,
        QuizzMapper quizzMapper,
        QuizzAnswerRepository quizzAnswerRepository,
        QuizzAnswerMapper quizzAnswerMapper,
        QuizzCategoryRepository quizzCategoryRepository,
        MetafieldRepository metafieldRepository,
        QuizzTransactionManager quizzTransactionManager,
        QuizzHelper quizzHelper
    ) {
        this.quizzRepository = quizzRepository;
        this.quizzMapper = quizzMapper;
        this.quizzAnswerRepository = quizzAnswerRepository;
        this.quizzAnswerMapper = quizzAnswerMapper;
        this.quizzCategoryRepository = quizzCategoryRepository;
        this.metafieldRepository = metafieldRepository;
        this.quizzTransactionManager = quizzTransactionManager;
        this.quizzHelper = quizzHelper;
    }

    /**
     * Save a quizz.
     *
     * @param quizzRequest the entity to save.
     * @return the persisted entity.
     */

    @Transactional
    public QuizzDTO save(QuizzRequest quizzRequest) {
        log.debug("Request to save Quizz : {}", quizzRequest);
        validateRequest(quizzRequest);
        QuizzBO quizzBO = QuizzMapper.INSTANCE.requestToBo(quizzRequest);
        quizzBO.validate();
        QuizzDTO quizzDTO = quizzTransactionManager.save(quizzBO);
        return quizzDTO;
    }

    private List<QuizzAnswerBO> getNonExistItems(QuizzRequest request, QuizzBO bo) {
        if (Objects.isNull(request.getAnswers()) || Objects.isNull(bo.getAnswers())) {
            return new ArrayList();
        }
        Set<Long> requestIds = request.getAnswers().stream()
            .filter(item -> item.getId() != null)
            .map(QuizzAnswerRequest::getId)
            .collect(Collectors.toSet());

        List<QuizzAnswerBO> filteredBOItems = bo.getAnswers().stream()
            .filter(item -> item.getId() == null || !requestIds.contains(item.getId()))
            .collect(Collectors.toList());

        return filteredBOItems;
    }
    /**
     * Update a quizz.
     *
     * @param quizzDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizzDTO update(QuizzRequest request) {
        log.debug("Request to update Quizz : {}", request);
        QuizzBO quizzBO = quizzTransactionManager.findById(request.getId());
        List<QuizzAnswerBO> orphanAnswers = getNonExistItems(request, quizzBO);
        List<MetafieldBO> orphanMetafields = quizzBO.getMetafields();

        quizzBO.update(request);
        quizzBO.validate();
        QuizzDTO quizzDTO = quizzTransactionManager.update(orphanMetafields, orphanAnswers, quizzBO);
        return quizzDTO;
    }

    /**
     * Partially update a quizz.
     *
     * @param quizzDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuizzDTO> partialUpdate(QuizzDTO quizzDTO) {
        log.debug("Request to partially update Quizz : {}", quizzDTO);

        return quizzRepository
            .findById(quizzDTO.getId())
            .map(existingQuizz -> {
                quizzMapper.partialUpdate(existingQuizz, quizzDTO);

                return existingQuizz;
            })
            .map(quizzRepository::save)
            .map(quizzMapper::toDto);
    }

    /**
     * Get all the quizzes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuizzDTO> findAll(Pageable pageable, QuizzSearchRequest request) {
        log.debug("Request to get all Quizzes");
        QuizzSpecification specification = new QuizzSpecification()
            .search(request.getSearch(), request.getType(), request.getCategoryId(), request.getQuizzIds());
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        Page<QuizzDTO> quizzDTOS = quizzRepository.findAll(specification, sortedPageable).map(quizzMapper::toDto);
        quizzHelper.enrichCategory(quizzDTOS.getContent());
        return quizzDTOS;
    }

    /**
     * Get one quizz by id.
     *
     * @param quizzDTOS the id of the entity.
     * @return the entity.
     */

    private void enrich(List<QuizzDTO> quizzDTOS) {
        quizzHelper.enrichMetafields(quizzDTOS);
        quizzHelper.enrichAnswers(quizzDTOS);
    }

    @Transactional(readOnly = true)
    public QuizzDTO findOne(Long id) {
        log.debug("Request to get Quizz : {}", id);
        Optional<QuizzDTO> quizzDTOOptional = quizzRepository.findById(id).map(QuizzMapper.INSTANCE::toDto);
        if (!quizzDTOOptional.isPresent()) {
            return null;
        }
        QuizzDTO quizzDTO = quizzDTOOptional.get();
        enrich(List.of(quizzDTO));
        return quizzDTO;
    }

    /**
     * Delete the quizz by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Quizz : {}", id);
        quizzRepository.deleteById(id);
        quizzAnswerRepository.deleteByRootId(id);
    }

    private void validateRequest(QuizzRequest quizzRequest) {
        ///validate if needed
        Long categoryId = quizzRequest.getCategoryId();
        Optional<QuizzCategoryEntity> cate = quizzCategoryRepository.findById(categoryId);
        if (cate.isEmpty()) {
            throw new NtsValidationException("message", String.format("category_id không hợp lệ", "category_id"));
        }
    }
}
