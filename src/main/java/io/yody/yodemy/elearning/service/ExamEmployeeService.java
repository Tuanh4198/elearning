package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.ExamEmployeeEntity;
import io.yody.yodemy.elearning.domain.ExamEmployeeResultEntity;
import io.yody.yodemy.elearning.domain.ExamEntity;
import io.yody.yodemy.elearning.domain.enumeration.ExamEmployeeStatusEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamQuizzMetafieldEnum;
import io.yody.yodemy.elearning.domain.enumeration.QuizzTypeEnum;
import io.yody.yodemy.elearning.repository.ExamEmployeeRepository;
import io.yody.yodemy.elearning.repository.ExamEmployeeResultRepository;
import io.yody.yodemy.elearning.repository.ExamRepository;
import io.yody.yodemy.elearning.service.dto.*;
import io.yody.yodemy.elearning.service.helpers.ExamEmployeeHelper;
import io.yody.yodemy.elearning.service.helpers.ExamHelper;
import io.yody.yodemy.elearning.service.helpers.Helper;
import io.yody.yodemy.elearning.service.helpers.QuizzHelper;
import io.yody.yodemy.elearning.service.mapper.ExamEmployeeMapper;
import io.yody.yodemy.elearning.service.mapper.ExamEmployeeResultMapper;
import io.yody.yodemy.elearning.service.mapper.ExamMapper;
import io.yody.yodemy.elearning.web.rest.vm.request.ExamEmployeeAnswerRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.ExamEmployeeSubmitRequest;
import org.nentangso.core.service.errors.NtsValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ExamEmployeeEntity}.
 */
@Service
@Transactional
public class ExamEmployeeService {

    private final Logger log = LoggerFactory.getLogger(ExamEmployeeService.class);

    private final ExamEmployeeRepository examEmployeeRepository;

    private final ExamEmployeeHelper examEmployeeHelper;
    private final ExamHelper examHelper;
    private final ExamRepository examRepository;
    private final ExamEmployeeResultRepository examEmployeeResultRepository;
    private final QuizzHelper quizzHelper;

    public ExamEmployeeService(
        ExamEmployeeRepository examEmployeeRepository,
        ExamEmployeeHelper examEmployeeHelper,
        ExamHelper examHelper,
        ExamRepository examRepository,
        ExamEmployeeResultRepository examEmployeeResultRepository,
        QuizzHelper quizzHelper
    ) {
        this.examEmployeeRepository = examEmployeeRepository;
        this.examEmployeeHelper = examEmployeeHelper;
        this.examHelper = examHelper;
        this.examRepository = examRepository;
        this.examEmployeeResultRepository = examEmployeeResultRepository;
        this.quizzHelper = quizzHelper;
    }

    /**
     * Save a examEmployee.
     *
     * @param examEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    public ExamEmployeeDTO save(ExamEmployeeDTO examEmployeeDTO) {
        log.debug("Request to save ExamEmployee : {}", examEmployeeDTO);
        ExamEmployeeEntity examEmployeeEntity = ExamEmployeeMapper.INSTANCE.toEntity(examEmployeeDTO);
        examEmployeeEntity = examEmployeeRepository.save(examEmployeeEntity);
        return ExamEmployeeMapper.INSTANCE.toDto(examEmployeeEntity);
    }

    /**
     * Update a examEmployee.
     *
     * @param examEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    public ExamEmployeeDTO update(ExamEmployeeDTO examEmployeeDTO) {
        log.debug("Request to update ExamEmployee : {}", examEmployeeDTO);
        ExamEmployeeEntity examEmployeeEntity = ExamEmployeeMapper.INSTANCE.toEntity(examEmployeeDTO);
        examEmployeeEntity = examEmployeeRepository.save(examEmployeeEntity);
        return ExamEmployeeMapper.INSTANCE.toDto(examEmployeeEntity);
    }

    private void validateWorkingTime(ExamDTO examDTO, Instant startAt, Instant finishedAt) {

    }

    private void validateNumberOfTest(ExamDTO examDTO, Long examEmployeeId) {

    }

    private ExamEmployeeResultEntity validateResult(ExamEmployeeSubmitRequest request, ExamEmployeeDTO examEmployeeDTO) {
        List<ExamEmployeeAnswerRequest> answers = request.getAnswers();
        List<QuizzDTO> quizzs = examEmployeeDTO.getQuizzs();

        Long numberOfCorrect = answers
            .stream()
            .filter(answer -> {
                Optional<QuizzDTO> quizzOptional = quizzs.stream().filter(quizz -> quizz.getId().equals(answer.getQuizzId())).findFirst();

                if (quizzOptional.isPresent()) {
                    QuizzDTO quizz = quizzOptional.get();
                    QuizzTypeEnum quizzType = quizz.getType();
                    if (!quizzType.equals(QuizzTypeEnum.MULTIPLE_CHOICE)) return true;
                    if (Objects.isNull(quizz.getMetafields())) return false;
                    Optional<String> correctAnswerOptional = quizz
                        .getMetafields()
                        .stream()
                        .filter(meta -> ExamQuizzMetafieldEnum.ANSWER.getKey().equals(meta.getKey()))
                        .map(MetafieldDTO::getValue)
                        .findFirst();

                    if (correctAnswerOptional.isPresent()) {
                        String correctAnswer = correctAnswerOptional.get();
                        return correctAnswer.equals(answer.getAnswer());
                    }
                }

                return false;
            })
            .count();
        Long numberOfQuestion = quizzs.size() * 1L;
        Long minPointToPass = examEmployeeDTO.getExam().getMinPointToPass();

        boolean isPass = ((double) numberOfCorrect / numberOfQuestion) >= (minPointToPass / 100.0);

        ExamEmployeeResultEntity result = new ExamEmployeeResultEntity()
            .startAt(request.getStartAt())
            .finishedAt(request.getFinishedAt())
            .rootId(examEmployeeDTO.getId())
            .numberOfQuestion(numberOfQuestion)
            .numberOfCorrect(numberOfCorrect)
            .minPointToPass(minPointToPass)
            .pass(isPass);
        return result;
    }

    private void saveResult(ExamEmployeeEntity examEmployee, ExamEmployeeResultEntity resultEntity) {
        resultEntity = examEmployeeResultRepository.save(resultEntity);
        if (resultEntity.isPass()) {
            examEmployee.setStatus(ExamEmployeeStatusEnum.PASS);
            examEmployeeRepository.save(examEmployee);
        }
    }

    private ExamEntity findExam(Long examId) {
        Optional<ExamEntity> examEntityOptional = examRepository.findById(examId);
        if (!examEntityOptional.isPresent()) {
            throw new NtsValidationException("message", "Không tìm thấy bài kiểm tra");
        }
        ExamEntity exam = examEntityOptional.get();
        return exam;
    }

    private ExamEmployeeEntity getExamEmployee(Long examId) {
        String userCode = Helper.getUserCodeUpperCase();
        List<ExamEmployeeEntity> examEmployees = examEmployeeRepository.findByCodeAndRootId(userCode, examId);
        ExamEmployeeEntity examEmployee = examEmployees.stream().findFirst().orElse(null);

        return examEmployee;
    }

    public ExamEmployeeResultDTO submit(ExamEmployeeSubmitRequest request) {
        Long examId = request.getRootId();
        ExamEmployeeEntity examEmployee = getExamEmployee(examId);
        if (Objects.isNull(examEmployee)) {
            throw new NtsValidationException("message", "Chưa được gán bài kiểm tra");
        }
        if (examEmployee.getStatus().equals(ExamEmployeeStatusEnum.PASS)) {
            throw new NtsValidationException("message", "Đã hoàn thành bài kiểm tra");
        }

        if (examEmployee.getStatus().equals(ExamEmployeeStatusEnum.NOT_ATTENDED)) {
            examEmployee.setStatus(ExamEmployeeStatusEnum.NOT_PASS);
        }
        ExamEntity exam = findExam(examId);

        if (!Objects.isNull(exam.getExpireTime())) {
            if (exam.getExpireTime().isBefore(Instant.now())) {
                throw new NtsValidationException("message", "Quá hạn làm bài kiểm tra");
            }
        }
        ExamDTO examDTO = ExamMapper.INSTANCE.toDto(exam);
        examHelper.enrichMetafields(List.of(examDTO));
        validateWorkingTime(examDTO, request.getStartAt(), request.getFinishedAt());
        validateNumberOfTest(examDTO, examEmployee.getId());

        ExamEmployeeDTO examEmployeeDTO = ExamEmployeeMapper.INSTANCE.toDto(examEmployee);
        examEmployeeDTO.setExam(examDTO);
        examEmployeeHelper.enrichQuizzs(List.of(examEmployeeDTO));

        List<QuizzDTO> quizzDTOS = examEmployeeDTO.getQuizzs();
        quizzHelper.enrichMetafields(quizzDTOS);
        quizzHelper.enrichAnswers(quizzDTOS);

        ExamEmployeeResultEntity resultEntity = validateResult(request, examEmployeeDTO);
        saveResult(examEmployee, resultEntity);
        ExamEmployeeResultDTO resultDTO = ExamEmployeeResultMapper.INSTANCE.toDto(resultEntity);
        return resultDTO;
    }

    public Boolean exists(Long examId) {
        log.debug("Request to get check exists exam employee : {}", examId);
        String userCode = Helper.getUserCodeUpperCase();
        List<ExamEmployeeEntity> examEmployees = examEmployeeRepository.findByCodeAndRootId(userCode, examId);
        ExamEmployeeEntity examEmployee = examEmployees.stream().findFirst().orElse(null);
        if (Objects.isNull(examEmployee)) {
            return false;
        }
        return true;
    }

    /**
     * Partially update a examEmployee.
     *
     * @param examEmployeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExamEmployeeDTO> partialUpdate(ExamEmployeeDTO examEmployeeDTO) {
        log.debug("Request to partially update ExamEmployee : {}", examEmployeeDTO);

        return examEmployeeRepository
            .findById(examEmployeeDTO.getId())
            .map(existingExamEmployee -> {
                ExamEmployeeMapper.INSTANCE.partialUpdate(existingExamEmployee, examEmployeeDTO);

                return existingExamEmployee;
            })
            .map(examEmployeeRepository::save)
            .map(ExamEmployeeMapper.INSTANCE::toDto);
    }

    /**
     * Delete the examEmployee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExamEmployee : {}", id);
        examEmployeeRepository.deleteById(id);
    }
}
