package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.QuizzEntity;
import io.yody.yodemy.elearning.service.business.QuizzBO;
import io.yody.yodemy.elearning.service.dto.QuizzDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for the entity {@link QuizzEntity} and its DTO {@link QuizzDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuizzMapper extends EntityMapper<QuizzDTO, QuizzEntity> {
    QuizzMapper INSTANCE = Mappers.getMapper(QuizzMapper.class);
    QuizzEntity requestToEntity(QuizzRequest quizzRequest);
    QuizzBO requestToBo(QuizzRequest quizzRequest);
    QuizzEntity boToEntity(QuizzBO quizzBO);
    QuizzBO entityToBo(QuizzEntity quizzEntity);
}
