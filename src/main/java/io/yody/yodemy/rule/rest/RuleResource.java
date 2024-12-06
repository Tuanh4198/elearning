package io.yody.yodemy.rule.rest;

import io.yody.yodemy.rule.knowledgeBase.KnowledgeBaseService;
import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;
import io.yody.yodemy.rule.knowledgeBase.request.RuleRequest;
import io.yody.yodemy.rule.ruleImpl.nodeRuleEngine.NodeDetail;
import io.yody.yodemy.rule.ruleImpl.nodeRuleEngine.NodeValidDetail;
import io.yody.yodemy.rule.service.NodeRuleService;
import org.nentangso.core.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class RuleResource {
    private final Logger log = LoggerFactory.getLogger(RuleResource.class);
    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private static final String ENTITY_NAME = "rule";
    private final KnowledgeBaseService knowledgeBaseService;
    private final NodeRuleService nodeRuleService;

    public RuleResource(KnowledgeBaseService knowledgeBaseService, NodeRuleService nodeRuleService) {
        this.knowledgeBaseService = knowledgeBaseService;
        this.nodeRuleService = nodeRuleService;
    }

    @PostMapping("/rules")
    public ResponseEntity<RuleDTO> createRule(@Valid @RequestBody RuleRequest request) throws URISyntaxException {
        log.debug("REST request to save Rule : {}", request);
        if (request.getId() != null) {
            throw new BadRequestAlertException("A new rule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RuleDTO result = knowledgeBaseService.create(request);
        return ResponseEntity
            .created(new URI("/api/rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/rules")
    public ResponseEntity<RuleDTO> updateRule(@Valid @RequestBody RuleRequest request) throws URISyntaxException {
        log.debug("REST request to save Rule : {}", request);
        RuleDTO result = knowledgeBaseService.update(request);
        return ResponseEntity
            .created(new URI("/api/rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/validate-node")
    public ResponseEntity<NodeValidDetail> validateNode(@RequestBody NodeDetail node) throws URISyntaxException {
        NodeValidDetail result = nodeRuleService.validate(node);
        return ResponseEntity
            .created(new URI("/api/validate-node/"))
            .body(result);
    }
}
