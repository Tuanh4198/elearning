package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.CourseEntity;
import io.yody.yodemy.elearning.service.business.CourseBO;
import io.yody.yodemy.elearning.service.dto.CourseDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.CourseRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for the entity {@link CourseEntity} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourseMapper extends EntityMapper<CourseDTO, CourseEntity> {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);
    CourseBO requestToBo(CourseRequest courseRequest);
    CourseEntity boToEntity(CourseBO courseBO);
    CourseBO entityToBo(CourseEntity courseEntity);
    CourseDTO boToDto(CourseBO courseBO);
    CourseBO dtoToBo(CourseDTO courseBO);
}
