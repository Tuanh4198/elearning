package io.yody.yodemy.elearning.service.mapper;

import io.yody.yodemy.elearning.domain.NodeEntity;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface NodeBaseMapper {
    @Named("mapToNode")
    default NodeEntity mapToNode(Long id) {
        if (id == null) {
            return null;
        }
        return new NodeEntity(id);
    }
    @Named("mapToRule")
    default RuleEntity mapToRule(Long id) {
        if (id == null) {
            return null;
        }
        return new RuleEntity(id);
    }

    @Named("mapToRootId")
    default Long mapToRootId(NodeEntity node) {
        if (Objects.isNull(node)) {
            return null;
        }
        return node.getId();
    }
}
