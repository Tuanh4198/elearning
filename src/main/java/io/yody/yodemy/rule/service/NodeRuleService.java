package io.yody.yodemy.rule.service;

import io.yody.yodemy.rule.rest.RuleResource;
import io.yody.yodemy.rule.ruleEngine.RuleEngine;
import io.yody.yodemy.rule.ruleImpl.nodeRuleEngine.NodeDetail;
import io.yody.yodemy.rule.ruleImpl.nodeRuleEngine.NodeInferenceEngine;
import io.yody.yodemy.rule.ruleImpl.nodeRuleEngine.NodeValidDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NodeRuleService {
    private final Logger log = LoggerFactory.getLogger(NodeRuleService.class);

    private RuleEngine ruleEngine;
    private NodeInferenceEngine nodeInferenceEngine;

    public NodeRuleService(RuleEngine ruleEngine, NodeInferenceEngine nodeInferenceEngine) {
        this.ruleEngine = ruleEngine;
        this.nodeInferenceEngine = nodeInferenceEngine;
    }

    public NodeValidDetail validate(NodeDetail nodeDetail) {
        NodeValidDetail detail = (NodeValidDetail) ruleEngine.run(nodeInferenceEngine, nodeDetail, nodeDetail.getId());
        return detail;
    }
}
