package io.yody.yodemy.elearning.web.rest.vm.request;

import io.yody.yodemy.rule.knowledgeBase.request.RuleRequest;

import java.math.BigDecimal;
import java.util.List;

public class NodeRequest {

    private Long id;
    private Long tempId;
    private Long flowId;
    private Long rootId;
    private String type;
    private String status;
    private String label;
    private BigDecimal positionX;
    private BigDecimal positionY;
    private BigDecimal width;
    private BigDecimal height;
    private List<MetafieldRequest> metafields;
    private List<RuleRequest> rules;
    private String thumbUrl;

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

    public List<MetafieldRequest> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldRequest> metafields) {
        this.metafields = metafields;
    }

    public List<RuleRequest> getRules() {
        return rules;
    }

    public void setRules(List<RuleRequest> rules) {
        this.rules = rules;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
