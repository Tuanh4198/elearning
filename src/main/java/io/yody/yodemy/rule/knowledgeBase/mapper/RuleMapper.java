package io.yody.yodemy.rule.knowledgeBase.mapper;

import io.yody.yodemy.elearning.service.business.DocumentRuleBO;
import io.yody.yodemy.elearning.service.business.ExamRuleBO;
import io.yody.yodemy.elearning.service.business.RuleBO;
import io.yody.yodemy.elearning.service.mapper.NodeBaseMapper;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;
import io.yody.yodemy.rule.knowledgeBase.request.RuleRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@ComponentScan(basePackages = "io.yody.yodemy")
@Mapper(componentModel = "spring", uses = { NodeBaseMapper.class })
public interface RuleMapper {
    RuleMapper INSTANCE = Mappers.getMapper(RuleMapper.class);
    RuleEntity boToEntity(RuleBO bo);
    RuleBO entityToBo(RuleEntity entity);
    List<RuleEntity> bosToEntities(List<RuleBO> bo);
    List<RuleEntity> documentBosToEntities(List<DocumentRuleBO> bo);
    List<RuleDTO> toDtos(List<RuleEntity> entities);
    RuleDTO toDto(RuleEntity entities);
    RuleEntity requestToEntity(RuleRequest request);
    List<RuleEntity> examBosToEntities(List<ExamRuleBO> bo);
    List<ExamRuleBO> examEntitiesToBos(List<RuleEntity> bo);

}
