package io.yody.yodemy.rule.knowledgeBase.dto;

import io.yody.yodemy.rule.knowledgeBase.domain.RuleNamespace;

public class RuleDTO {
    private RuleNamespace namespace;
    private Long id;
    private Long rootId;
    private String condition;
    private String action;
    private Integer priority;
    private String description;

    public RuleDTO() {
    }

    public RuleDTO(RuleNamespace namespace, Long id, Long rootId, String condition, String action, Integer priority, String description) {
        this.namespace = namespace;
        this.id = id;
        this.rootId = rootId;
        this.condition = condition;
        this.action = action;
        this.priority = priority;
        this.description = description;
    }

    public RuleNamespace getRuleNamespace() {
        return namespace;
    }

    public void setRuleNamespace(RuleNamespace ruleNamespace) {
        this.namespace = ruleNamespace;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RuleNamespace getNamespace() {
        return namespace;
    }

    public void setNamespace(RuleNamespace namespace) {
        this.namespace = namespace;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
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

    public RuleDTO namespace(RuleNamespace namespace) {
        this.namespace = namespace;
        return this;
    }

    public RuleDTO id(Long id) {
        this.id = id;
        return this;
    }

    public RuleDTO condition(String condition) {
        this.condition = condition;
        return this;
    }

    public RuleDTO action(String action) {
        this.action = action;
        return this;
    }

    public RuleDTO priority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public RuleDTO description(String description) {
        this.description = description;
        return this;
    }

}
