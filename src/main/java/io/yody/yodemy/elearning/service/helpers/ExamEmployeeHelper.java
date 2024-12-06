package io.yody.yodemy.elearning.service.helpers;

import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.*;

import io.yody.yodemy.domain.MetafieldEntity;
import io.yody.yodemy.elearning.domain.ExamEntity;
import io.yody.yodemy.elearning.domain.QuizzAnswerEntity;
import io.yody.yodemy.elearning.domain.QuizzEntity;
import io.yody.yodemy.elearning.domain.enumeration.ExamEmployeeMetafieldEnum;
import io.yody.yodemy.elearning.repository.ExamRepository;
import io.yody.yodemy.elearning.repository.MetafieldRepository;
import io.yody.yodemy.elearning.repository.QuizzAnswerRepository;
import io.yody.yodemy.elearning.repository.QuizzRepository;
import io.yody.yodemy.elearning.service.dto.*;
import io.yody.yodemy.elearning.service.mapper.ExamMapper;
import io.yody.yodemy.elearning.service.mapper.MetafieldMapper;
import io.yody.yodemy.elearning.service.mapper.QuizzAnswerMapper;
import io.yody.yodemy.elearning.service.mapper.QuizzMapper;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ExamEmployeeHelper {

    private final MetafieldRepository metafieldRepository;
    private final ExamRepository examRepository;
    private final QuizzRepository quizzRepository;
    private final QuizzAnswerRepository quizzAnswerRepository;

    public ExamEmployeeHelper(
        MetafieldRepository metafieldRepository,
        ExamRepository examRepository,
        QuizzRepository quizzRepository,
        QuizzAnswerRepository quizzAnswerRepository
    ) {
        this.metafieldRepository = metafieldRepository;
        this.examRepository = examRepository;
        this.quizzRepository = quizzRepository;
        this.quizzAnswerRepository = quizzAnswerRepository;
    }

    public void enrichQuizzs(List<ExamEmployeeDTO> examEmployeeDTOS) {
        List<Long> examEmployeeIds = examEmployeeDTOS.stream().map(ExamEmployeeDTO::getId).collect(Collectors.toList());
        List<MetafieldEntity> metafields = metafieldRepository.findAllByOwnerResourceAndOwnerIdIn(
            EXAM_EMPLOYEE_OWNER_RESOURCE,
            examEmployeeIds
        );
        List<MetafieldDTO> metafieldDTOS = MetafieldMapper.INSTANCE.toDto(metafields);

        List<Long> quizzIds = new ArrayList<>();
        Map<Long, List<Long>> examEmployeeIdQuizzsMap = new HashMap<>();
        for (MetafieldDTO metafieldDTO : metafieldDTOS) {
            List<Long> examEmployeeQuizzs = examEmployeeIdQuizzsMap.get(metafieldDTO.getOwnerId());
            if (Objects.isNull(examEmployeeQuizzs)) {
                examEmployeeQuizzs = new ArrayList<>();
                examEmployeeIdQuizzsMap.put(metafieldDTO.getOwnerId(), examEmployeeQuizzs);
            }
            if (metafieldDTO.getKey().equals(ExamEmployeeMetafieldEnum.QUIZZ_ID.getKey())) {
                Long quizzId = Long.valueOf(metafieldDTO.getValue());
                if (!quizzIds.contains(quizzId)) {
                    quizzIds.add(quizzId);
                }
                if (!examEmployeeQuizzs.contains(quizzId)) {
                    examEmployeeQuizzs.add(quizzId);
                }
            }
        }

        List<QuizzEntity> quizzs = quizzRepository.findAllByIdIn(quizzIds);
        List<QuizzDTO> quizzDTOS = QuizzMapper.INSTANCE.toDto(quizzs);

        for (ExamEmployeeDTO examEmployeeDTO : examEmployeeDTOS) {
            List<Long> examEmployeeQuizzIds = examEmployeeIdQuizzsMap.get(examEmployeeDTO.getId());
            if (Objects.isNull(examEmployeeQuizzIds)) continue;
            List<QuizzDTO> examEmployeeQuizzDTOS = quizzDTOS
                .stream()
                .filter(quizzDTO -> examEmployeeQuizzIds.contains(quizzDTO.getId()))
                .collect(Collectors.toList());
            examEmployeeDTO.setQuizzs(examEmployeeQuizzDTOS);
        }
    }

    public void enrichExam(List<ExamEmployeeDTO> examEmployeeDTOS) {
        List<Long> examIds = examEmployeeDTOS.stream().map(ExamEmployeeDTO::getRootId).collect(Collectors.toList());
        List<ExamEntity> exams = examRepository.findAllByIdIn(examIds);
        List<ExamDTO> examDTOS = ExamMapper.INSTANCE.toDto(exams);
        Map<Long, ExamDTO> examDTOMap = examDTOS.stream().collect(Collectors.toMap(ExamDTO::getId, Function.identity()));

        for (ExamEmployeeDTO examEmployeeDTO : examEmployeeDTOS) {
            if (!Objects.isNull(examEmployeeDTO.getRootId())) {
                examEmployeeDTO.setExam(examDTOMap.get(examEmployeeDTO.getRootId()));
            }
        }
    }
}
