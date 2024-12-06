package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.EdgeEntity;
import io.yody.yodemy.elearning.service.dto.EdgeDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.EdgeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EdgeMapper extends EntityMapper<EdgeDTO, EdgeEntity> {
    EdgeMapper INSTANCE = Mappers.getMapper(EdgeMapper.class);
    EdgeEntity requestToEntity(EdgeRequest request);
}
