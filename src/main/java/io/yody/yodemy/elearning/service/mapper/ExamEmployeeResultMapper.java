package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.ExamEmployeeResultEntity;
import io.yody.yodemy.elearning.service.dto.ExamEmployeeResultDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for the entity {@link ExamEmployeeResultEntity} and its DTO {@link ExamEmployeeResultDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExamEmployeeResultMapper extends EntityMapper<ExamEmployeeResultDTO, ExamEmployeeResultEntity> {
    ExamEmployeeResultMapper INSTANCE = Mappers.getMapper(ExamEmployeeResultMapper.class);
}
