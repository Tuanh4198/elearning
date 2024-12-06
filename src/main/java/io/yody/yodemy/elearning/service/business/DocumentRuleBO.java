package io.yody.yodemy.elearning.service.business;

import io.yody.yodemy.rule.knowledgeBase.domain.RuleNamespace;
import io.yody.yodemy.rule.knowledgeBase.request.RuleRequest;
import org.apache.commons.lang3.ObjectUtils;

public class DocumentRuleBO {

    private Long id;
    private Long rootId;
    private RuleNamespace namespace;
    private String condition;
    private String action;
    private Integer priority;
    private String description;

    public DocumentRuleBO() {}

    public DocumentRuleBO(DocumentBO aggregate, RuleRequest request) {
        this.id = request.getId();
        this.rootId = aggregate.getId();
        this.namespace = request.getNamespace();
        this.condition = request.getCondition();
        this.action = request.getAction();
        this.priority = request.getPriority();
        this.description = request.getDescription();
        refineCondition();
    }

    public void update(DocumentBO aggregate, RuleRequest request) {
        this.id = request.getId();
        this.rootId = aggregate.getId();
        this.namespace = request.getNamespace();
        this.condition = request.getCondition();
        this.action = request.getAction();
        this.priority = request.getPriority();
        this.description = request.getDescription();
        refineCondition();
    }

    private void refineCondition() {
        if (ObjectUtils.isEmpty(this.condition)) {
            this.condition = "true";
        };
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public RuleNamespace getNamespace() {
        return namespace;
    }

    public void setNamespace(RuleNamespace namespace) {
        this.namespace = namespace;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
