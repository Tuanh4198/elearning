package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.QuizzAnswerEntity;
import io.yody.yodemy.elearning.service.business.QuizzAnswerBO;
import io.yody.yodemy.elearning.service.dto.QuizzAnswerDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzAnswerRequest;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper for the entity {@link QuizzAnswerEntity} and its DTO {@link QuizzAnswerDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuizzAnswerMapper extends EntityMapper<QuizzAnswerDTO, QuizzAnswerEntity> {
    QuizzAnswerMapper INSTANCE = Mappers.getMapper(QuizzAnswerMapper.class);
    QuizzAnswerEntity toAnswerEntity(QuizzAnswerRequest quizzAnswerRequest);
    List<QuizzAnswerEntity> bosToEntities(List<QuizzAnswerBO> bos);
    List<QuizzAnswerBO> entitiesToBos(List<QuizzAnswerEntity> quizzAnswerEntities);
    List<QuizzAnswerDTO> entitiesToDtos(List<QuizzAnswerEntity> quizzAnswerEntities);

}
