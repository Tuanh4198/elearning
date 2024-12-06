package io.yody.yodemy.rule.knowledgeBase;

import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleNamespace;
import io.yody.yodemy.rule.knowledgeBase.repository.RuleRepository;
import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;
import io.yody.yodemy.rule.knowledgeBase.mapper.RuleMapper;
import io.yody.yodemy.rule.knowledgeBase.request.RuleRequest;
import org.nentangso.core.service.errors.NtsValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class KnowledgeBaseService {
    private final RuleRepository rulesRepository;

    public KnowledgeBaseService(RuleRepository rulesRepository) {
        this.rulesRepository = rulesRepository;
    }

    public List<RuleDTO> getAllRuleByNamespaceAndRootId(RuleNamespace namespace, Long rootId) {
        List<RuleEntity> rules;
        if (!Objects.isNull(rootId)) {
            rules = rulesRepository.findByNamespaceAndRootId(String.valueOf(namespace), rootId);
        } else {
            rules = rulesRepository.findByNamespace(String.valueOf(namespace));
        }
        return rules.stream()
            .map(
                RuleMapper.INSTANCE::toDto
            )
            .collect(Collectors.toList());
    }

    public RuleDTO create(RuleRequest request) {
        boolean duplicateExists = rulesRepository.existsByNamespaceAndRootId(String.valueOf(request.getNamespace()), request.getRootId());
        if (duplicateExists) {
            throw new NtsValidationException("message", "Rule đã tồn tại");
        }

        RuleEntity rule = RuleMapper.INSTANCE.requestToEntity(request);
        rule = rulesRepository.save(rule);
        RuleDTO dto = RuleMapper.INSTANCE.toDto(rule);
        return dto;
    }

    public RuleDTO update(RuleRequest request) {
        Long id = request.getId();
        RuleEntity existingRule = rulesRepository.findById(id)
            .orElseThrow(() -> new NtsValidationException("message", "Rule với " + id + " không tồn tại."));

        RuleEntity updatedRule = RuleMapper.INSTANCE.requestToEntity(request);
        updatedRule.setId(existingRule.getId());
        updatedRule = rulesRepository.save(updatedRule);

        return RuleMapper.INSTANCE.toDto(updatedRule);
    }
}

