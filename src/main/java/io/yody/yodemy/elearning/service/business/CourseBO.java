package io.yody.yodemy.elearning.service.business;

import io.yody.yodemy.elearning.domain.enumeration.AssignStrategyEnum;
import io.yody.yodemy.elearning.service.helpers.UpdateListHelper;
import io.yody.yodemy.elearning.web.rest.vm.request.CourseRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.DocumentRequest;
import org.nentangso.core.service.errors.NtsValidationException;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class CourseBO {

    private Long id;
    private String title;
    private Long categoryId;
    private Boolean requireJoin;
    private Boolean requireAttend;
    private String meetingUrl;
    private String meetingPassword;
    private Long examId;
    private Instant applyTime;
    private Instant expireTime;
    private String description;
    private String thumbUrl;
    private List<DocumentBO> documents;
    private CategoryBO category;
    private List<MetafieldBO> metafields;
    private Long nodeId;

    public CourseBO(
        Long id,
        String title,
        Long categoryId,
        Boolean requireJoin,
        Boolean requireAttend,
        String meetingUrl,
        String meetingPassword,
        Long examId,
        Instant applyTime,
        Instant expireTime,
        AssignStrategyEnum assignStrategy,
        String assignStrategyJson,
        String description,
        String thumbUrl,
        List<DocumentBO> documents,
        CategoryBO category,
        List<MetafieldBO> metafields,
        Long nodeId
    ) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.requireJoin = requireJoin;
        this.requireAttend = requireAttend;
        this.meetingUrl = meetingUrl;
        this.meetingPassword = meetingPassword;
        this.examId = examId;
        this.applyTime = applyTime;
        this.expireTime = expireTime;
        this.description = description;
        this.thumbUrl = thumbUrl;
        this.documents = documents;
        this.category = category;
        this.metafields = metafields;
        this.nodeId = nodeId;
    }

    public void update(CourseRequest request) {
        this.id = request.getId();
        this.title = request.getTitle();
        this.categoryId = request.getCategoryId();
        this.requireJoin = request.getRequireJoin();
        this.requireAttend = request.getRequireAttend();
        this.meetingUrl = request.getMeetingUrl();
        this.meetingPassword = request.getMeetingPassword();
        this.examId = request.getExamId();
        this.applyTime = request.getApplyTime();
        this.expireTime = request.getExpireTime();
        this.description = request.getDescription();
        this.thumbUrl = request.getThumbUrl();
        this.nodeId = request.getNodeId();
        updateDocuments(request.getDocuments());
    }

    private void updateDocuments(List<DocumentRequest> requests) {
        UpdateListHelper.updateListBO(this, documents, requests, DocumentBO::getId, DocumentRequest::getId,
            DocumentBO::update, DocumentBO::new);
    }

    public CategoryBO getCategory() {
        return category;
    }

    public void setCategory(CategoryBO category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getRequireJoin() {
        return requireJoin;
    }

    public void setRequireJoin(Boolean requireJoin) {
        this.requireJoin = requireJoin;
    }

    public Boolean getRequireAttend() {
        return requireAttend;
    }

    public void setRequireAttend(Boolean requireAttend) {
        this.requireAttend = requireAttend;
    }

    public String getMeetingUrl() {
        return meetingUrl;
    }

    public void setMeetingUrl(String meetingUrl) {
        this.meetingUrl = meetingUrl;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Instant getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Instant applyTime) {
        this.applyTime = applyTime;
    }

    public Instant getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Instant expireTime) {
        this.expireTime = expireTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public List<DocumentBO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentBO> documents) {
        this.documents = documents;
    }

    public List<MetafieldBO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldBO> metafields) {
        this.metafields = metafields;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getMeetingPassword() {
        return meetingPassword;
    }

    public void setMeetingPassword(String meetingPassword) {
        this.meetingPassword = meetingPassword;
    }

    private void validateTime() {
        if (!Objects.isNull(applyTime) && !Objects.isNull(expireTime) && applyTime.isAfter(expireTime)) {
            throw new NtsValidationException("message", "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc");
        }
    }

    private void validateDocuments() {
        if (documents.isEmpty()) {
            throw new NtsValidationException("message", "Khóa học phải có tài liệu");
        }
        for (DocumentBO documentBO : documents) {
            documentBO.validate();
        }
    }

    public void validate() {
        validateTime();
        validateDocuments();
    }
}
