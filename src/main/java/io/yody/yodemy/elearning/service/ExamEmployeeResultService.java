package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.ExamEmployeeResultEntity;
import io.yody.yodemy.elearning.repository.ExamEmployeeResultRepository;
import io.yody.yodemy.elearning.service.dto.ExamEmployeeResultDTO;
import io.yody.yodemy.elearning.service.mapper.ExamEmployeeResultMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExamEmployeeResultEntity}.
 */
@Service
@Transactional
public class ExamEmployeeResultService {

    private final Logger log = LoggerFactory.getLogger(ExamEmployeeResultService.class);

    private final ExamEmployeeResultRepository examEmployeeResultRepository;

    private final ExamEmployeeResultMapper examEmployeeResultMapper;

    public ExamEmployeeResultService(
        ExamEmployeeResultRepository examEmployeeResultRepository,
        ExamEmployeeResultMapper examEmployeeResultMapper
    ) {
        this.examEmployeeResultRepository = examEmployeeResultRepository;
        this.examEmployeeResultMapper = examEmployeeResultMapper;
    }

    /**
     * Save a examEmployeeResult.
     *
     * @param examEmployeeResultDTO the entity to save.
     * @return the persisted entity.
     */
    public ExamEmployeeResultDTO save(ExamEmployeeResultDTO examEmployeeResultDTO) {
        log.debug("Request to save ExamEmployeeResult : {}", examEmployeeResultDTO);
        ExamEmployeeResultEntity examEmployeeResultEntity = examEmployeeResultMapper.toEntity(examEmployeeResultDTO);
        examEmployeeResultEntity = examEmployeeResultRepository.save(examEmployeeResultEntity);
        return examEmployeeResultMapper.toDto(examEmployeeResultEntity);
    }

    /**
     * Update a examEmployeeResult.
     *
     * @param examEmployeeResultDTO the entity to save.
     * @return the persisted entity.
     */
    public ExamEmployeeResultDTO update(ExamEmployeeResultDTO examEmployeeResultDTO) {
        log.debug("Request to update ExamEmployeeResult : {}", examEmployeeResultDTO);
        ExamEmployeeResultEntity examEmployeeResultEntity = examEmployeeResultMapper.toEntity(examEmployeeResultDTO);
        examEmployeeResultEntity = examEmployeeResultRepository.save(examEmployeeResultEntity);
        return examEmployeeResultMapper.toDto(examEmployeeResultEntity);
    }

    /**
     * Partially update a examEmployeeResult.
     *
     * @param examEmployeeResultDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExamEmployeeResultDTO> partialUpdate(ExamEmployeeResultDTO examEmployeeResultDTO) {
        log.debug("Request to partially update ExamEmployeeResult : {}", examEmployeeResultDTO);

        return examEmployeeResultRepository
            .findById(examEmployeeResultDTO.getId())
            .map(existingExamEmployeeResult -> {
                examEmployeeResultMapper.partialUpdate(existingExamEmployeeResult, examEmployeeResultDTO);

                return existingExamEmployeeResult;
            })
            .map(examEmployeeResultRepository::save)
            .map(examEmployeeResultMapper::toDto);
    }

    /**
     * Get all the examEmployeeResults.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExamEmployeeResultDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExamEmployeeResults");
        return examEmployeeResultRepository.findAll(pageable).map(examEmployeeResultMapper::toDto);
    }

    /**
     * Get one examEmployeeResult by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExamEmployeeResultDTO> findOne(Long id) {
        log.debug("Request to get ExamEmployeeResult : {}", id);
        return examEmployeeResultRepository.findById(id).map(examEmployeeResultMapper::toDto);
    }

    /**
     * Delete the examEmployeeResult by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExamEmployeeResult : {}", id);
        examEmployeeResultRepository.deleteById(id);
    }
}
