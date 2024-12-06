package io.yody.yodemy.elearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.yody.yodemy.elearning.domain.enumeration.AssignStrategyEnum;
import io.yody.yodemy.elearning.domain.enumeration.CourseEmployeeStatusEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link io.yody.yodemy.elearning.domain.CourseEntity} entity.
 */
@Schema(description = "Khóa học")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourseDTO implements Serializable {

    /**
     * id
     */
    @NotNull
    @Schema(description = "id", required = true)
    private Long id;

    /**
     * title
     */
    @NotNull
    @Size(min = 1, max = 255)
    @Schema(description = "title", required = true)
    private String title;

    /**
     * Id danh mục
     */
    @NotNull
    @Schema(description = "Id danh mục", required = true)
    private Long categoryId;

    /**
     * Bắt buộc tham gia
     */
    @Schema(description = "Bắt buộc tham gia")
    private Boolean requireJoin;

    /**
     * Yêu cầu điểm danh
     */
    @Schema(description = "Yêu cầu điểm danh")
    private Boolean requireAttend;

    /**
     * Phòng đào tạo
     */
    @Schema(description = "Phòng đào tạo")
    private String meetingUrl;

    @Schema(description = "MK Phòng đào tạo")
    private String meetingPassword;

    /**
     * Id kỳ thi
     */
    @Schema(description = "Id kỳ thi")
    private Long examId;

    /**
     * Thời gian bắt đầu
     */
    @NotNull
    @Schema(description = "Thời gian bắt đầu", required = true)
    private Instant applyTime;

    /**
     * Thời gian kết thúc
     */
    @Schema(description = "Thời gian kết thúc")
    private Instant expireTime;

    /**
     * Đối tượng tham gia
     */
    @Schema(description = "Đối tượng tham gia")
    private AssignStrategyEnum assignStrategy;

    /**
     * Danh sách đối tượng tham gia, ngăn cách bởi dấu ,
     */
    @Schema(description = "Danh sách đối tượng tham gia, ngăn cách bởi dấu ,")
    private String assignStrategyJson;

    /**
     * Mô tả
     */
    @Schema(description = "Mô tả")
    private String description;

    /**
     * Ảnh bìa
     */
    @Schema(description = "Ảnh bìa")
    private String thumbUrl;

    @Schema(description =  "Tài liệu")
    private List<DocumentDTO> documents;

    private CategoryDTO category;

    @Schema(description = "metafields")
    private List<MetafieldDTO> metafields;

    private Long nodeId;
    private CourseEmployeeStatusEnum status;

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
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

    public AssignStrategyEnum getAssignStrategy() {
        return assignStrategy;
    }

    public void setAssignStrategy(AssignStrategyEnum assignStrategy) {
        this.assignStrategy = assignStrategy;
    }

    public String getAssignStrategyJson() {
        return assignStrategyJson;
    }

    public void setAssignStrategyJson(String assignStrategyJson) {
        this.assignStrategyJson = assignStrategyJson;
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

    public List<DocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentDTO> documents) {
        this.documents = documents;
    }

    public List<MetafieldDTO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldDTO> metafields) {
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

    public CourseEmployeeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CourseEmployeeStatusEnum status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseDTO)) {
            return false;
        }

        CourseDTO courseDTO = (CourseDTO) o;
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
            ", assignStrategy='" + getAssignStrategy() + "'" +
            ", assignStrategyJson='" + getAssignStrategyJson() + "'" +
            ", description='" + getDescription() + "'" +
            ", thumbUrl='" + getThumbUrl() + "'" +
            ", thumbUrl='" + getDocuments() + "'" +
            ", metafields='" + getMetafields() + "'" +
            "}";
    }
}
