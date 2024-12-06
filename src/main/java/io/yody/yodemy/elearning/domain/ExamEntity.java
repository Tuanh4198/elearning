package io.yody.yodemy.elearning.domain;

import io.yody.yodemy.elearning.domain.enumeration.ExamPointStrategyEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamQuizzPoolStrategyEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamStrategyEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.nentangso.core.domain.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * Kỳ thi
 */
@Entity
@Table(name = "exam")
@Where(clause = "deleted = false")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamEntity extends AbstractAuditingEntity implements Serializable {

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
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * Kỳ thi bắt buộc
     */
    @Column(name = "require_join")
    private Boolean requireJoin;

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
     * Cách chấm điểm
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "point_strategy", nullable = false)
    private ExamPointStrategyEnum pointStrategy;

    /**
     * Điểm tối thiểu cần đạt
     */
    @NotNull
    @Column(name = "min_point_to_pass", nullable = false)
    private Long minPointToPass;

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

    /**
     * Id bài đào tạo
     */
    @Column(name = "course_id")
    private Long courseId;

    /**
     * Tổng số lượng câu hỏi
     */
    @NotNull
    @Column(name = "number_of_question", nullable = false)
    private Long numberOfQuestion;

    /**
     * Chiến thuật chọn pool câu hỏi
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "pool_strategy")
    private ExamQuizzPoolStrategyEnum poolStrategy;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_strategy")
    private ExamStrategyEnum examStrategy;

    @Column(name = "node_id")
    private Long nodeId;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExamEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public ExamEntity title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public ExamEntity categoryId(Long categoryId) {
        this.setCategoryId(categoryId);
        return this;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getRequireJoin() {
        return this.requireJoin;
    }

    public ExamEntity requireJoin(Boolean requireJoin) {
        this.setRequireJoin(requireJoin);
        return this;
    }

    public void setRequireJoin(Boolean requireJoin) {
        this.requireJoin = requireJoin;
    }

    public Instant getApplyTime() {
        return this.applyTime;
    }

    public ExamEntity applyTime(Instant applyTime) {
        this.setApplyTime(applyTime);
        return this;
    }

    public void setApplyTime(Instant applyTime) {
        this.applyTime = applyTime;
    }

    public Instant getExpireTime() {
        return this.expireTime;
    }

    public ExamEntity expireTime(Instant expireTime) {
        this.setExpireTime(expireTime);
        return this;
    }

    public void setExpireTime(Instant expireTime) {
        this.expireTime = expireTime;
    }

    public ExamPointStrategyEnum getPointStrategy() {
        return this.pointStrategy;
    }

    public ExamEntity pointStrategy(ExamPointStrategyEnum pointStrategy) {
        this.setPointStrategy(pointStrategy);
        return this;
    }

    public void setPointStrategy(ExamPointStrategyEnum pointStrategy) {
        this.pointStrategy = pointStrategy;
    }

    public Long getMinPointToPass() {
        return this.minPointToPass;
    }

    public ExamEntity minPointToPass(Long minPointToPass) {
        this.setMinPointToPass(minPointToPass);
        return this;
    }

    public void setMinPointToPass(Long minPointToPass) {
        this.minPointToPass = minPointToPass;
    }

    public String getDescription() {
        return this.description;
    }

    public ExamEntity description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbUrl() {
        return this.thumbUrl;
    }

    public ExamEntity thumbUrl(String thumbUrl) {
        this.setThumbUrl(thumbUrl);
        return this;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Long getCourseId() {
        return this.courseId;
    }

    public ExamEntity courseId(Long courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getNumberOfQuestion() {
        return this.numberOfQuestion;
    }

    public ExamEntity numberOfQuestion(Long numberOfQuestion) {
        this.setNumberOfQuestion(numberOfQuestion);
        return this;
    }

    public void setNumberOfQuestion(Long numberOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
    }

    public ExamQuizzPoolStrategyEnum getPoolStrategy() {
        return this.poolStrategy;
    }

    public ExamEntity poolStrategy(ExamQuizzPoolStrategyEnum poolStrategy) {
        this.setPoolStrategy(poolStrategy);
        return this;
    }

    public void setPoolStrategy(ExamQuizzPoolStrategyEnum poolStrategy) {
        this.poolStrategy = poolStrategy;
    }

    public ExamStrategyEnum getExamStrategy() {
        return examStrategy;
    }

    public void setExamStrategy(ExamStrategyEnum examStrategy) {
        this.examStrategy = examStrategy;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamEntity)) {
            return false;
        }
        return id != null && id.equals(((ExamEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", categoryId=" + getCategoryId() +
            ", requireJoin='" + getRequireJoin() + "'" +
            ", applyTime='" + getApplyTime() + "'" +
            ", expireTime='" + getExpireTime() + "'" +
            ", pointStrategy='" + getPointStrategy() + "'" +
            ", minPointToPass=" + getMinPointToPass() +
            ", description='" + getDescription() + "'" +
            ", thumbUrl='" + getThumbUrl() + "'" +
            ", courseId=" + getCourseId() +
            ", numberOfQuestion=" + getNumberOfQuestion() +
            ", strategy='" + getPoolStrategy() + "'" +
            "}";
    }
}
