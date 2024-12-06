package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.NodeMetafieldEntity;
import io.yody.yodemy.elearning.service.business.NodeMetafieldBO;
import io.yody.yodemy.elearning.service.dto.NodeMetafieldDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NodeMetafieldMapper extends EntityMapper<NodeMetafieldDTO, NodeMetafieldEntity> {
    NodeMetafieldMapper INSTANCE = Mappers.getMapper(NodeMetafieldMapper.class);
    List<NodeMetafieldBO> entitiesToBos(List<NodeMetafieldEntity> metafields);
    NodeMetafieldEntity nodeMetafieldBoToEntity(NodeMetafieldBO metafieldBO);
    List<NodeMetafieldEntity> bosToEntites(List<NodeMetafieldBO> metafieldBOS);
}
