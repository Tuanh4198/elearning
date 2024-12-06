package io.yody.yodemy.rule.knowledgeBase.request;

import io.yody.yodemy.rule.knowledgeBase.domain.RuleNamespace;

public class RuleRequest {
    private Long id;
    private Long tempId;
    private Long rootId;
    private RuleNamespace namespace;
    private String condition;
    private String action;
    private Integer priority;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTempId() {
        return tempId;
    }

    public void setTempId(Long tempId) {
        this.tempId = tempId;
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
