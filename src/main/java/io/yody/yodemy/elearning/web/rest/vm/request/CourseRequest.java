package io.yody.yodemy.elearning.web.rest.vm.request;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class CourseRequest {
    private Long id;

    /**
     * title
     */
    @NotNull
    private String title;

    /**
     * Id danh mục
     */
    @NotNull
    private Long categoryId;

    /**
     * Bắt buộc tham gia
     */
    private Boolean requireJoin;

    /**
     * Yêu cầu điểm danh
     */
    private Boolean requireAttend;

    /**
     * Phòng đào tạo
     */
    private String meetingUrl;

    /**
     * mk đào tạo
     */
    private String meetingPassword;

    /**
     * Id kỳ thi
     */
    private Long examId;

    /**
     * Thời gian bắt đầu
     */
    @NotNull
    private Instant applyTime;

    /**
     * Thời gian kết thúc
     */
    private Instant expireTime;

    /**
     * Mô tả
     */
    private String description;

    /**
     * Ảnh bìa
     */
    private String thumbUrl;

    @NotNull
    private List<DocumentRequest> documents;

    private Long nodeId;

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

    public void setDocuments(List<DocumentRequest> documents) {
        this.documents = documents;
    }

    public List<DocumentRequest> getDocuments() {
        return documents;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseRequest)) {
            return false;
        }

        CourseRequest courseDTO = (CourseRequest) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, courseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", categoryId=" + getCategoryId() +
            ", requireJoin='" + getRequireJoin() + "'" +
            ", requireAttend='" + getRequireAttend() + "'" +
            ", meetingUrl='" + getMeetingUrl() + "'" +
            ", examId=" + getExamId() +
            ", applyTime='" + getApplyTime() + "'" +
            ", expireTime='" + getExpireTime() + "'" +
            ", description='" + getDescription() + "'" +
            ", thumbUrl='" + getThumbUrl() + "'" +
            "}";
    }
}
