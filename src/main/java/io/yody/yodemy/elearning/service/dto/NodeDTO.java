package io.yody.yodemy.elearning.service.dto;

import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class NodeDTO implements Serializable {
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
    private List<NodeMetafieldDTO> metafields;
    private List<RuleDTO> rules;
    private Boolean validTime = true;
    private Boolean validEmployee = true;
    private CourseDTO course;
    private String thumbUrl;
    private String erThumbUrl;
    private ExamDTO exam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<NodeMetafieldDTO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<NodeMetafieldDTO> metafields) {
        this.metafields = metafields;
    }

    public List<RuleDTO> getRules() {
        return rules;
    }

    public void setRules(List<RuleDTO> rules) {
        this.rules = rules;
    }

    public Boolean getValidTime() {
        return validTime;
    }

    public void setValidTime(Boolean validTime) {
        this.validTime = validTime;
    }

    public Boolean getValidEmployee() {
        return validEmployee;
    }

    public void setValidEmployee(Boolean validEmployee) {
        this.validEmployee = validEmployee;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getErThumbUrl() {
        return erThumbUrl;
    }

    public void setErThumbUrl(String erThumbUrl) {
        this.erThumbUrl = erThumbUrl;
    }

    public ExamDTO getExam() {
        return exam;
    }

    public void setExam(ExamDTO exam) {
        this.exam = exam;
    }
}
