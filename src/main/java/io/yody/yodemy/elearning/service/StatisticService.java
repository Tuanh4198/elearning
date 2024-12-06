package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.repository.CourseEmployeeRepository;
import io.yody.yodemy.elearning.repository.ExamEmployeeRepository;
import io.yody.yodemy.elearning.service.dto.GeneralStatisticDTO;
import io.yody.yodemy.elearning.service.helpers.Helper;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {

    private ExamEmployeeRepository examEmployeeRepository;
    private CourseEmployeeRepository courseEmployeeRepository;

    public StatisticService(ExamEmployeeRepository examEmployeeRepository, CourseEmployeeRepository courseEmployeeRepository) {
        this.examEmployeeRepository = examEmployeeRepository;
        this.courseEmployeeRepository = courseEmployeeRepository;
    }

    public GeneralStatisticDTO getGeneralStatistic() {
        String userCode = Helper.getUserCodeUpperCase();
        if (Objects.isNull(userCode)) return new GeneralStatisticDTO();

        int totalExamEmployee = examEmployeeRepository.countTotalExamEmployee(userCode);
        int totalCompletedExamEmployee = examEmployeeRepository.countPassedExamEmployee(userCode);

        int totalCourseEmployee = courseEmployeeRepository.countTotalCourseEmployee(userCode);
        int totalCompletedCourseEmployee = courseEmployeeRepository.countLearnedCourseEmployee(userCode);

        GeneralStatisticDTO statistic = new GeneralStatisticDTO()
            .totalExam(totalExamEmployee)
            .totalCompletedExam(totalCompletedExamEmployee)
            .totalCourse(totalCourseEmployee)
            .totalCompletedCourse(totalCompletedCourseEmployee);

        return statistic;
    }
}
