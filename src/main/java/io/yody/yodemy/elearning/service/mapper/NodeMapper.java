package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.NodeEntity;
import io.yody.yodemy.elearning.service.business.NodeAggregate;
import io.yody.yodemy.elearning.service.business.RuleBO;
import io.yody.yodemy.elearning.service.dto.NodeDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.NodeRequest;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import io.yody.yodemy.rule.knowledgeBase.mapper.RuleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {NodeBaseMapper.class, RuleMapper.class})
public interface NodeMapper extends EntityMapper<NodeDTO, NodeEntity> {
    NodeMapper INSTANCE = Mappers.getMapper(NodeMapper.class);
    NodeEntity requestToEntity(NodeRequest request);
    NodeAggregate toAggregate(NodeEntity entity);
    NodeEntity aggregateToEntity(NodeAggregate aggregate);
    NodeDTO toDto(NodeEntity entity);
}
