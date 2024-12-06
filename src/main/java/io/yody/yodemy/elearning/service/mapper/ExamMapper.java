package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.ExamEntity;
import io.yody.yodemy.elearning.service.business.ExamBO;
import io.yody.yodemy.elearning.service.dto.ExamDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.ExamRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for the entity {@link ExamEntity} and its DTO {@link ExamDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExamMapper extends EntityMapper<ExamDTO, ExamEntity> {
    ExamMapper INSTANCE = Mappers.getMapper(ExamMapper.class);
    ExamEntity requestToEntity(ExamRequest examRequest);
    ExamBO requestToBo(ExamRequest examRequest);
    ExamEntity boToEntity(ExamBO examBO);
    ExamBO entityToBo(ExamEntity examEntity);
}
