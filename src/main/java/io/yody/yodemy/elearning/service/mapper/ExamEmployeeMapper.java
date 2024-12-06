package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.ExamEmployeeEntity;
import io.yody.yodemy.elearning.service.dto.ExamEmployeeDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for the entity {@link ExamEmployeeEntity} and its DTO {@link ExamEmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExamEmployeeMapper extends EntityMapper<ExamEmployeeDTO, ExamEmployeeEntity> {
    ExamEmployeeMapper INSTANCE = Mappers.getMapper(ExamEmployeeMapper.class);
}
