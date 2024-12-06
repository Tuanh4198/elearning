package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.elearning.domain.NodeEntity;
import io.yody.yodemy.elearning.domain.NodeMetafieldEntity;
import io.yody.yodemy.elearning.repository.NodeMetafieldRepository;
import io.yody.yodemy.elearning.repository.NodeRepository;
import io.yody.yodemy.elearning.service.business.NodeAggregate;
import io.yody.yodemy.elearning.service.business.NodeMetafieldBO;
import io.yody.yodemy.elearning.service.business.RuleBO;
import io.yody.yodemy.elearning.service.dto.NodeDTO;
import io.yody.yodemy.elearning.service.mapper.NodeMapper;
import io.yody.yodemy.elearning.service.mapper.NodeMetafieldMapper;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import io.yody.yodemy.rule.knowledgeBase.mapper.RuleMapper;
import io.yody.yodemy.rule.knowledgeBase.repository.RuleRepository;
import org.nentangso.core.service.errors.NtsValidationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class NodeTransactionManager {

    private final NodeRepository nodeRepository;
    private final NodeMapper nodeMapper;
    private final NodeMetafieldRepository nodeMetafieldRepository;
    private final RuleRepository ruleRepository;

    public NodeTransactionManager(NodeRepository nodeRepository, NodeMapper nodeMapper,
                                  NodeMetafieldRepository nodeMetafieldRepository, RuleRepository ruleRepository) {
        this.nodeRepository = nodeRepository;
        this.nodeMapper = nodeMapper;
        this.nodeMetafieldRepository = nodeMetafieldRepository;
        this.ruleRepository = ruleRepository;
    }

    public List<NodeMetafieldEntity> getMetafieldsFromAggregate(NodeAggregate aggregate) {
        List<NodeMetafieldEntity> metafields = new ArrayList<>();

        List<NodeMetafieldBO> metafieldBOS = aggregate.getMetafields();
        metafields.addAll(NodeMetafieldMapper.INSTANCE.bosToEntites(metafieldBOS));

        return metafields;
    }

    private void deleteMetafields(List<NodeMetafieldEntity> metafields) {
        nodeMetafieldRepository.deleteAll(metafields);
    }

    private void saveMetafields(List<NodeAggregate> aggregates) {
        Set<NodeMetafieldEntity> uniqueMetafields = new HashSet<>();

        for (NodeAggregate aggregate : aggregates) {
            List<NodeMetafieldEntity> metafields = getMetafieldsFromAggregate(aggregate);
            uniqueMetafields.addAll(metafields);
        }
        // Convert the Set back to a List if required by the repository method
        List<NodeMetafieldEntity> allMetafields = new ArrayList<>(uniqueMetafields);
        nodeMetafieldRepository.saveAll(allMetafields);
    }

    private void saveRules(NodeAggregate aggregate) {
        List<RuleBO> rules = aggregate.getRules();
        List<RuleEntity> ruleEntities = RuleMapper.INSTANCE.bosToEntities(rules);
        ruleRepository.saveAll(ruleEntities);
    }

    private void deleteRules(List<RuleEntity> rules) {
        rules.forEach(rule -> {
            rule.setDeleted(true);
        });
        ruleRepository.saveAll(rules);
    }

    @Transactional
    public NodeDTO save(NodeAggregate aggregate) {
        NodeEntity entity = this.nodeMapper.aggregateToEntity(aggregate);
        entity = nodeRepository.save(entity);
        saveMetafields(List.of(aggregate));
        NodeDTO surveyDTO = this.nodeMapper.toDto(entity);
        saveRules(aggregate);
        return surveyDTO;
    }

    @Transactional
    public NodeDTO update(List<NodeMetafieldEntity> oldMetafields, List<RuleEntity> orphanRules, NodeAggregate aggregate) {
        deleteMetafields(oldMetafields);
        deleteRules(orphanRules);
        NodeEntity entity = this.nodeMapper.aggregateToEntity(aggregate);
        entity = nodeRepository.save(entity);
        saveMetafields(List.of(aggregate));
        NodeDTO surveyDTO = this.nodeMapper.toDto(entity);
        saveRules(aggregate);
        return surveyDTO;
    }

    @Transactional
    public List<NodeDTO> updateMultiple(List<NodeMetafieldEntity> oldMetafields, List<NodeAggregate> aggregates) {
        deleteMetafields(oldMetafields);
        List<NodeEntity> entities = aggregates.stream().map(nodeMapper::aggregateToEntity).collect(Collectors.toList());

        entities = nodeRepository.saveAll(entities);
        saveMetafields(aggregates);
        return entities.stream().map(nodeMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NodeAggregate findById(Long nodeId) {
        NodeEntity entity = nodeRepository.findById(nodeId).filter(Predicate.not(NodeEntity::isDeleted)).orElse(null);
        if (entity == null) {
            throw new NtsValidationException("message", String.format("Không tìm thấy Node %s", nodeId));
        }
        NodeAggregate aggregate = nodeMapper.toAggregate(entity);
        return aggregate;
    }

    @Transactional(readOnly = true)
    public List<NodeAggregate> findAllByIds(List<Long> nodeIds) {
        List<NodeEntity> entities = nodeRepository
            .findAllById(nodeIds)
            .stream()
            .filter(Predicate.not(NodeEntity::isDeleted))
            .collect(Collectors.toList());

        if (entities.isEmpty()) {
            return new ArrayList<>();
        }

        return entities.stream().map(nodeMapper::toAggregate).collect(Collectors.toList());
    }
}
