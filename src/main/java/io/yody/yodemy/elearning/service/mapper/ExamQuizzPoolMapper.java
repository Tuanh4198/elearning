package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.ExamQuizzPoolEntity;
import io.yody.yodemy.elearning.service.business.ExamQuizzPoolBO;
import io.yody.yodemy.elearning.service.dto.ExamQuizzPoolDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.ExamQuizzPoolRequest;
import java.util.List;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for the entity {@link ExamQuizzPoolEntity} and its DTO {@link ExamQuizzPoolDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExamQuizzPoolMapper extends EntityMapper<ExamQuizzPoolDTO, ExamQuizzPoolEntity> {
    ExamQuizzPoolMapper INSTANCE = Mappers.getMapper(ExamQuizzPoolMapper.class);
    ExamQuizzPoolEntity requestToEntity(ExamQuizzPoolRequest examQuizzPoolRequest);
    List<ExamQuizzPoolBO> entitiesToBos(List<ExamQuizzPoolEntity> entities);
    List<ExamQuizzPoolEntity> bosToEntities(List<ExamQuizzPoolBO> examQuizzPoolBOS);
    List<ExamQuizzPoolDTO> entitiesToDtos(List<ExamQuizzPoolEntity> entities);
    ExamQuizzPoolBO requestToBo(ExamQuizzPoolRequest request);
}
