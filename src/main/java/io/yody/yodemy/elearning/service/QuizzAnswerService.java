package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.QuizzAnswerEntity;
import io.yody.yodemy.elearning.repository.QuizzAnswerRepository;
import io.yody.yodemy.elearning.service.dto.QuizzAnswerDTO;
import io.yody.yodemy.elearning.service.mapper.QuizzAnswerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuizzAnswerEntity}.
 */
@Service
@Transactional
public class QuizzAnswerService {

    private final Logger log = LoggerFactory.getLogger(QuizzAnswerService.class);

    private final QuizzAnswerRepository quizzAnswerRepository;

    private final QuizzAnswerMapper quizzAnswerMapper;

    public QuizzAnswerService(QuizzAnswerRepository quizzAnswerRepository, QuizzAnswerMapper quizzAnswerMapper) {
        this.quizzAnswerRepository = quizzAnswerRepository;
        this.quizzAnswerMapper = quizzAnswerMapper;
    }

    /**
     * Save a quizzAnswer.
     *
     * @param quizzAnswerDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizzAnswerDTO save(QuizzAnswerDTO quizzAnswerDTO) {
        log.debug("Request to save QuizzAnswer : {}", quizzAnswerDTO);
        QuizzAnswerEntity quizzAnswerEntity = quizzAnswerMapper.toEntity(quizzAnswerDTO);
        quizzAnswerEntity = quizzAnswerRepository.save(quizzAnswerEntity);
        return quizzAnswerMapper.toDto(quizzAnswerEntity);
    }

    /**
     * Update a quizzAnswer.
     *
     * @param quizzAnswerDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizzAnswerDTO update(QuizzAnswerDTO quizzAnswerDTO) {
        log.debug("Request to update QuizzAnswer : {}", quizzAnswerDTO);
        QuizzAnswerEntity quizzAnswerEntity = quizzAnswerMapper.toEntity(quizzAnswerDTO);
        quizzAnswerEntity = quizzAnswerRepository.save(quizzAnswerEntity);
        return quizzAnswerMapper.toDto(quizzAnswerEntity);
    }

    /**
     * Partially update a quizzAnswer.
     *
     * @param quizzAnswerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuizzAnswerDTO> partialUpdate(QuizzAnswerDTO quizzAnswerDTO) {
        log.debug("Request to partially update QuizzAnswer : {}", quizzAnswerDTO);

        return quizzAnswerRepository
            .findById(quizzAnswerDTO.getId())
            .map(existingQuizzAnswer -> {
                quizzAnswerMapper.partialUpdate(existingQuizzAnswer, quizzAnswerDTO);

                return existingQuizzAnswer;
            })
            .map(quizzAnswerRepository::save)
            .map(quizzAnswerMapper::toDto);
    }

    /**
     * Get all the quizzAnswers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuizzAnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuizzAnswers");
        return quizzAnswerRepository.findAll(pageable).map(quizzAnswerMapper::toDto);
    }

    /**
     * Get one quizzAnswer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuizzAnswerDTO> findOne(Long id) {
        log.debug("Request to get QuizzAnswer : {}", id);
        return quizzAnswerRepository.findById(id).map(quizzAnswerMapper::toDto);
    }

    /**
     * Delete the quizzAnswer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuizzAnswer : {}", id);
        quizzAnswerRepository.deleteById(id);
    }
}
