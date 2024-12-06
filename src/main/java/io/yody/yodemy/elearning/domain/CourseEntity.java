package io.yody.yodemy.elearning.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.nentangso.core.domain.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * Khóa học
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourseEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * title
     */
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    /**
     * Id danh mục
     */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    /**
     * Bắt buộc tham gia
     */
    @Column(name = "require_join")
    private Boolean requireJoin;

    /**
     * Yêu cầu điểm danh
     */
    @Column(name = "require_attend")
    private Boolean requireAttend;

    /**
     * Phòng đào tạo
     */
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "meeting_url")
    private String meetingUrl;

    @Column(name = "meeting_password")
    private String meetingPassword;

    /**
     * Id kỳ thi
     */
    @Column(name = "exam_id")
    private Long examId;

    /**
     * Thời gian bắt đầu
     */
    @NotNull
    @Column(name = "apply_time", nullable = false)
    private Instant applyTime;

    /**
     * Thời gian kết thúc
     */
    @Column(name = "expire_time")
    private Instant expireTime;

    /**
     * Mô tả
     */
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    /**
     * Ảnh bìa
     */
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "thumb_url")
    private String thumbUrl;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "node_id")
    private Long nodeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CourseEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public CourseEntity title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public CourseEntity categoryId(Long categoryId) {
        this.setCategoryId(categoryId);
        return this;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getRequireJoin() {
        return this.requireJoin;
    }

    public CourseEntity requireJoin(Boolean requireJoin) {
        this.setRequireJoin(requireJoin);
        return this;
    }

    public void setRequireJoin(Boolean requireJoin) {
        this.requireJoin = requireJoin;
    }

    public Boolean getRequireAttend() {
        return this.requireAttend;
    }

    public CourseEntity requireAttend(Boolean requireAttend) {
        this.setRequireAttend(requireAttend);
        return this;
    }

    public void setRequireAttend(Boolean requireAttend) {
        this.requireAttend = requireAttend;
    }

    public String getMeetingUrl() {
        return this.meetingUrl;
    }

    public CourseEntity meetingUrl(String meetingUrl) {
        this.setMeetingUrl(meetingUrl);
        return this;
    }

    public void setMeetingUrl(String meetingUrl) {
        this.meetingUrl = meetingUrl;
    }

    public String getMeetingPassword() {
        return meetingPassword;
    }

    public void setMeetingPassword(String meetingPassword) {
        this.meetingPassword = meetingPassword;
    }

    public Long getExamId() {
        return this.examId;
    }

    public CourseEntity examId(Long examId) {
        this.setExamId(examId);
        return this;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Instant getApplyTime() {
        return this.applyTime;
    }

    public CourseEntity applyTime(Instant applyTime) {
        this.setApplyTime(applyTime);
        return this;
    }

    public void setApplyTime(Instant applyTime) {
        this.applyTime = applyTime;
    }

    public Instant getExpireTime() {
        return this.expireTime;
    }

    public CourseEntity expireTime(Instant expireTime) {
        this.setExpireTime(expireTime);
        return this;
    }

    public void setExpireTime(Instant expireTime) {
        this.expireTime = expireTime;
    }

    public String getDescription() {
        return this.description;
    }

    public CourseEntity description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbUrl() {
        return this.thumbUrl;
    }

    public CourseEntity thumbUrl(String thumbUrl) {
        this.setThumbUrl(thumbUrl);
        return this;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseEntity)) {
            return false;
        }
        return id != null && id.equals(((CourseEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseEntity{" +
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
