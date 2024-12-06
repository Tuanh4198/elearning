package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.ExamQuizzPoolEntity;
import io.yody.yodemy.elearning.repository.ExamQuizzPoolRepository;
import io.yody.yodemy.elearning.service.dto.ExamQuizzPoolDTO;
import io.yody.yodemy.elearning.service.mapper.ExamQuizzPoolMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExamQuizzPoolEntity}.
 */
@Service
@Transactional
public class ExamQuizzPoolService {

    private final Logger log = LoggerFactory.getLogger(ExamQuizzPoolService.class);

    private final ExamQuizzPoolRepository examQuizzPoolRepository;

    private final ExamQuizzPoolMapper examQuizzPoolMapper;

    public ExamQuizzPoolService(ExamQuizzPoolRepository examQuizzPoolRepository, ExamQuizzPoolMapper examQuizzPoolMapper) {
        this.examQuizzPoolRepository = examQuizzPoolRepository;
        this.examQuizzPoolMapper = examQuizzPoolMapper;
    }

    /**
     * Save a examQuizzPool.
     *
     * @param examQuizzPoolDTO the entity to save.
     * @return the persisted entity.
     */
    public ExamQuizzPoolDTO save(ExamQuizzPoolDTO examQuizzPoolDTO) {
        log.debug("Request to save ExamQuizzPool : {}", examQuizzPoolDTO);
        ExamQuizzPoolEntity examQuizzPoolEntity = examQuizzPoolMapper.toEntity(examQuizzPoolDTO);
        examQuizzPoolEntity = examQuizzPoolRepository.save(examQuizzPoolEntity);
        return examQuizzPoolMapper.toDto(examQuizzPoolEntity);
    }

    /**
     * Update a examQuizzPool.
     *
     * @param examQuizzPoolDTO the entity to save.
     * @return the persisted entity.
     */
    public ExamQuizzPoolDTO update(ExamQuizzPoolDTO examQuizzPoolDTO) {
        log.debug("Request to update ExamQuizzPool : {}", examQuizzPoolDTO);
        ExamQuizzPoolEntity examQuizzPoolEntity = examQuizzPoolMapper.toEntity(examQuizzPoolDTO);
        examQuizzPoolEntity = examQuizzPoolRepository.save(examQuizzPoolEntity);
        return examQuizzPoolMapper.toDto(examQuizzPoolEntity);
    }

    /**
     * Partially update a examQuizzPool.
     *
     * @param examQuizzPoolDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExamQuizzPoolDTO> partialUpdate(ExamQuizzPoolDTO examQuizzPoolDTO) {
        log.debug("Request to partially update ExamQuizzPool : {}", examQuizzPoolDTO);

        return examQuizzPoolRepository
            .findById(examQuizzPoolDTO.getId())
            .map(existingExamQuizzPool -> {
                examQuizzPoolMapper.partialUpdate(existingExamQuizzPool, examQuizzPoolDTO);

                return existingExamQuizzPool;
            })
            .map(examQuizzPoolRepository::save)
            .map(examQuizzPoolMapper::toDto);
    }

    /**
     * Get all the examQuizzPools.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExamQuizzPoolDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExamQuizzPools");
        return examQuizzPoolRepository.findAll(pageable).map(examQuizzPoolMapper::toDto);
    }

    /**
     * Get one examQuizzPool by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExamQuizzPoolDTO> findOne(Long id) {
        log.debug("Request to get ExamQuizzPool : {}", id);
        return examQuizzPoolRepository.findById(id).map(examQuizzPoolMapper::toDto);
    }

    /**
     * Delete the examQuizzPool by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExamQuizzPool : {}", id);
        examQuizzPoolRepository.deleteById(id);
    }
}
