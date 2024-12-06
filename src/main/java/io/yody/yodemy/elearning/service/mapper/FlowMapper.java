package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.FlowEntity;
import io.yody.yodemy.elearning.service.dto.FlowDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlowMapper extends EntityMapper<FlowDTO, FlowEntity>{
}
