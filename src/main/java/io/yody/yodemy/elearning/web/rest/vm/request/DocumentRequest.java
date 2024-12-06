package io.yody.yodemy.elearning.web.rest.vm.request;

import io.yody.yodemy.rule.knowledgeBase.request.RuleRequest;

import java.util.List;

public class DocumentRequest {
    private Long id;

    private Long tempId;
    private Long rootId;

    private String content;
    private String name;
    private String type;
    private List<RuleRequest> rules;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<RuleRequest> getRules() {
        return rules;
    }

    public void setRules(List<RuleRequest> rules) {
        this.rules = rules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
