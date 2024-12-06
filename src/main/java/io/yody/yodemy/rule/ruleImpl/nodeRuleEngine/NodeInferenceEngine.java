package io.yody.yodemy.rule.ruleImpl.nodeRuleEngine;

import io.yody.yodemy.rule.knowledgeBase.domain.RuleNamespace;
import io.yody.yodemy.rule.langParser.RuleParser;
import io.yody.yodemy.rule.ruleEngine.InferenceEngine;
import org.springframework.stereotype.Service;

@Service
public class NodeInferenceEngine extends InferenceEngine<NodeDetail, NodeValidDetail> {

    public NodeInferenceEngine(RuleParser<NodeDetail, NodeValidDetail> ruleParser) {
        super(ruleParser);
    }

    @Override
    protected RuleNamespace getRuleNamespace() {
        return RuleNamespace.NODE_EMPLOYEE;
    }

    @Override
    protected NodeValidDetail initializeOutputResult() {
        return new NodeValidDetail();
    }
}

