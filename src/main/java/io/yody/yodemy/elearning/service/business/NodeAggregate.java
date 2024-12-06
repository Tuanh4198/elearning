package io.yody.yodemy.elearning.service.business;

import io.yody.yodemy.elearning.service.helpers.UpdateListHelper;
import io.yody.yodemy.elearning.web.rest.vm.request.MetafieldRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.NodeRequest;
import io.yody.yodemy.rule.knowledgeBase.request.RuleRequest;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NodeAggregate {

    private Long id;
    private Long flowId;
    private Long rootId;
    private String type;
    private String status;
    private String label;
    private BigDecimal positionX;
    private BigDecimal positionY;
    private BigDecimal width;
    private BigDecimal height;
    private List<NodeMetafieldBO> metafields;
    private List<RuleBO> rules;
    private String thumbUrl;

    public NodeAggregate() {}

    public NodeAggregate(NodeRequest nodeRequest) {
        this.id = nodeRequest.getTempId();
        this.flowId = nodeRequest.getFlowId();
        this.rootId = nodeRequest.getRootId();
        this.type = nodeRequest.getType();
        this.status = nodeRequest.getStatus();
        this.label = nodeRequest.getLabel();
        this.positionX = nodeRequest.getPositionX();
        this.positionY = nodeRequest.getPositionY();
        this.width = nodeRequest.getWidth();
        this.height = nodeRequest.getHeight();
        this.thumbUrl = nodeRequest.getThumbUrl();
        _setRules(nodeRequest.getRules());
        updateMetafields(nodeRequest.getMetafields());
    }

    public void update(NodeRequest request) {
        this.id = request.getTempId();
        this.flowId = request.getFlowId();
        this.rootId = request.getRootId();
        this.type = request.getType();
        this.status = request.getStatus();
        this.label = request.getLabel();
        this.positionX = request.getPositionX();
        this.positionY = request.getPositionY();
        this.width = request.getWidth();
        this.height = request.getHeight();
        this.thumbUrl = request.getThumbUrl();
        updateRules(request.getRules());
        updateMetafields(request.getMetafields());
    }

    private void updateRules(List<RuleRequest> requests) {
        UpdateListHelper.updateListBO(this, rules, requests, RuleBO::getId, RuleRequest::getTempId, RuleBO::update, RuleBO::new);
    }

    private void _setRules(List<RuleRequest> requests) {
        if (ObjectUtils.isEmpty(this.rules)) {
            this.rules = new ArrayList<>();
        }
        if (ObjectUtils.isEmpty(requests)) return;
        requests.forEach(request -> {
            this.rules.add(new RuleBO(this, request));
        });
    }

    private void updateMetafields(List<MetafieldRequest> requests) {
        this.metafields = new ArrayList<>();
        if (ObjectUtils.isEmpty(requests)) return;
        requests.forEach(request -> {
            this.metafields.add(new NodeMetafieldBO(this.getId(), request));
        });
    }

    public Long getId() {
        return id;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getPositionX() {
        return positionX;
    }

    public void setPositionX(BigDecimal positionX) {
        this.positionX = positionX;
    }

    public BigDecimal getPositionY() {
        return positionY;
    }

    public void setPositionY(BigDecimal positionY) {
        this.positionY = positionY;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public List<NodeMetafieldBO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<NodeMetafieldBO> metafields) {
        this.metafields = metafields;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RuleBO> getRules() {
        return rules;
    }

    public void setRules(List<RuleBO> rules) {
        this.rules = rules;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
