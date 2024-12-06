package io.yody.yodemy.elearning.service.business;

import io.yody.yodemy.elearning.service.helpers.UpdateListHelper;
import io.yody.yodemy.elearning.web.rest.vm.request.DocumentRequest;
import io.yody.yodemy.rule.knowledgeBase.request.RuleRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.nentangso.core.service.errors.NtsValidationException;

import java.util.ArrayList;
import java.util.List;

public class DocumentBO {

    private Long id;
    private Long rootId;
    private String content;
    private String name;
    private String type;
    private List<DocumentRuleBO> rules;

    public DocumentBO() {}
    public DocumentBO(Object root, DocumentRequest request) {
        setId(request.getId());
        setRootId(request.getRootId());
        setContent(request.getContent());
        setName(request.getName());
        setType(request.getType());
        _setRules(request.getRules());
    }

    public void update(Object root, DocumentRequest request) {
        setId(request.getId());
        setRootId(request.getRootId());
        setContent(request.getContent());
        setName(request.getName());
        setType(request.getType());
        updateRules(request.getRules());
    }

    private void updateRules(List<RuleRequest> requests) {
        UpdateListHelper.updateListBO(this, rules, requests, DocumentRuleBO::getId, RuleRequest::getTempId, DocumentRuleBO::update, DocumentRuleBO::new);
    }

    private void _setRules(List<RuleRequest> requests) {
        if (ObjectUtils.isEmpty(this.rules)) {
            this.rules = new ArrayList<>();
        }
        if (ObjectUtils.isEmpty(requests)) return;
        requests.forEach(request -> {
            this.rules.add(new DocumentRuleBO(this, request));
        });
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<DocumentRuleBO> getRules() {
        return rules;
    }

    public void setRules(List<DocumentRuleBO> rules) {
        this.rules = rules;
    }

    private void validateDocuments() {
        if (content.isEmpty()) {
            throw new NtsValidationException("message", "Tài liệu không đủ thông tin");
        }
    }

    public void validate() {
        validateDocuments();
    }
}
