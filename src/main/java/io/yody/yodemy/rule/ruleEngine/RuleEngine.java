package io.yody.yodemy.rule.ruleEngine;

import io.yody.yodemy.rule.knowledgeBase.KnowledgeBaseService;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleNamespace;
import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleEngine {
    private final KnowledgeBaseService knowledgeBaseService;

    public RuleEngine(KnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }

    public Object run(InferenceEngine inferenceEngine, Object inputData, Long rootId) {
        RuleNamespace ruleNamespace = inferenceEngine.getRuleNamespace();
        //TODO: Here for each call, we are fetching all rules from db. It should be cache.
        List<RuleDTO> allRulesByNamespace = knowledgeBaseService.getAllRuleByNamespaceAndRootId(ruleNamespace, rootId);
        Object result = inferenceEngine.run(allRulesByNamespace, inputData);
        return result;
    }

}
