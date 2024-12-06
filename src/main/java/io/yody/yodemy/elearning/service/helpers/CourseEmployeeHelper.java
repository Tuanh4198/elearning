package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.domain.MetafieldEntity;
import io.yody.yodemy.elearning.domain.CourseEntity;
import io.yody.yodemy.elearning.repository.CategoryRepository;
import io.yody.yodemy.elearning.repository.CourseRepository;
import io.yody.yodemy.elearning.repository.DocumentRepository;
import io.yody.yodemy.elearning.repository.MetafieldRepository;
import io.yody.yodemy.elearning.service.dto.CourseDTO;
import io.yody.yodemy.elearning.service.dto.CourseEmployeeDTO;
import io.yody.yodemy.elearning.service.dto.MetafieldDTO;
import io.yody.yodemy.elearning.service.dto.QuizzDTO;
import io.yody.yodemy.elearning.service.mapper.CourseMapper;
import io.yody.yodemy.elearning.service.mapper.MetafieldMapper;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.COURSE_EMPLOYEE_OWNER_RESOURCE;
import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.QUIZZANSWER;

@Component
public class CourseEmployeeHelper {

    private final MetafieldRepository metafieldRepository;
    private final CourseRepository courseRepository;
    private final DocumentRepository documentRepository;
    private final CategoryRepository categoryRepository;
    private final CourseHelper courseHelper;

    public CourseEmployeeHelper(
        MetafieldRepository metafieldRepository,
        CourseRepository courseRepository,
        DocumentRepository documentRepository,
        CategoryRepository categoryRepository,
        CourseHelper courseHelper
    ) {
        this.courseRepository = courseRepository;
        this.metafieldRepository = metafieldRepository;
        this.documentRepository = documentRepository;
        this.categoryRepository = categoryRepository;
        this.courseHelper = courseHelper;
    }

    public void enrichCourse(List<CourseEmployeeDTO> courseEmployeeDTOS) {
        List<Long> examIds = courseEmployeeDTOS.stream().map(CourseEmployeeDTO::getRootId).collect(Collectors.toList());
        List<CourseEntity> courses = courseRepository.findAllByIdIn(examIds);
        List<CourseDTO> courseDTOS = CourseMapper.INSTANCE.toDto(courses);
        Map<Long, CourseDTO> courseDTOMap = courseDTOS.stream().collect(Collectors.toMap(CourseDTO::getId, Function.identity()));

        for (CourseEmployeeDTO courseEmployeeDTO : courseEmployeeDTOS) {
            if (!Objects.isNull(courseEmployeeDTO.getRootId())) {
                courseEmployeeDTO.setCourse(courseDTOMap.get(courseEmployeeDTO.getRootId()));
            }
        }
    }

    public void enrichMetafields(List<CourseEmployeeDTO> courseEmployeeDTOS) {
        List<Long> courseEmployeeIds = courseEmployeeDTOS.stream().map(CourseEmployeeDTO::getId).collect(Collectors.toList());
        List<MetafieldEntity> metafields = metafieldRepository.findAllByOwnerResourceAndOwnerIdIn(COURSE_EMPLOYEE_OWNER_RESOURCE, courseEmployeeIds);
        List<MetafieldDTO> metafieldDTOS = MetafieldMapper.INSTANCE.toDto(metafields);
        Map<Long, List<MetafieldDTO>> metafieldDTOsMap = metafieldDTOS.stream().collect(Collectors.groupingBy(MetafieldDTO::getOwnerId));

        for (CourseEmployeeDTO courseEmployeeDTO : courseEmployeeDTOS) {
            if (!Objects.isNull(courseEmployeeDTO.getId())) {
                courseEmployeeDTO.setMetafields(metafieldDTOsMap.get(courseEmployeeDTO.getId()));
            }
        }
    }

    public void enrichCourseDetail(List<CourseEmployeeDTO> courseEmployeeDTOS) {
        List<Long> examIds = courseEmployeeDTOS.stream().map(CourseEmployeeDTO::getRootId).collect(Collectors.toList());
        List<CourseEntity> courses = courseRepository.findAllByIdIn(examIds);
        List<CourseDTO> courseDTOS = CourseMapper.INSTANCE.toDto(courses);
        courseHelper.enrichCourseEmployees(courseDTOS);
        for (CourseEmployeeDTO courseEmployeeDTO : courseEmployeeDTOS) {
            if (!Objects.isNull(courseEmployeeDTO.getRootId())) {
                Optional<CourseDTO> courseDTO = courseDTOS.stream().filter(e -> Objects.equals(e.getId(), courseEmployeeDTO.getRootId())).findFirst();
                courseDTO.ifPresent(courseEmployeeDTO::setCourse);
            }
        }
    }
}
