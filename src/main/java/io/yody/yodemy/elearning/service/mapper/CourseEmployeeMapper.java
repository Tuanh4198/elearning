package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.CourseEmployeeEntity;
import io.yody.yodemy.elearning.service.dto.CourseEmployeeDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for the entity {@link CourseEmployeeEntity} and its DTO {@link CourseEmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourseEmployeeMapper extends EntityMapper<CourseEmployeeDTO, CourseEmployeeEntity> {
    CourseEmployeeMapper INSTANCE = Mappers.getMapper(CourseEmployeeMapper.class);
}
