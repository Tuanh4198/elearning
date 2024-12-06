package io.yody.yodemy.elearning.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nentangso.core.domain.AbstractAuditingEntity;

/**
 * Kết quả bài thi của user
 */
@Entity
@Table(name = "exam_employee_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamEmployeeResultEntity extends AbstractAuditingEntity implements Serializable {

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
     * Id bài kiểm tra
     */
    @NotNull
    @Column(name = "root_id", nullable = false)
    private Long rootId;

    /**
     * Thời gian bắt đầu làm bài
     */
    @NotNull
    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    /**
     * Thời gian nộp bài
     */
    @NotNull
    @Column(name = "finished_at", nullable = false)
    private Instant finishedAt;

    /**
     * Tổng số câu đúng
     */
    @NotNull
    @Column(name = "number_of_correct", nullable = false)
    private Long numberOfCorrect;

    /**
     * numberOfQuestion
     */
    @NotNull
    @Column(name = "number_of_question", nullable = false)
    private Long numberOfQuestion;

    @NotNull
    @Column(name = "min_point_to_pass", nullable = false)
    private Long minPointToPass;

    @NotNull
    @Column(name = "is_pass", nullable = false)
    private boolean isPass = false;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExamEmployeeResultEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRootId() {
        return this.rootId;
    }

    public ExamEmployeeResultEntity rootId(Long rootId) {
        this.setRootId(rootId);
        return this;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Instant getStartAt() {
        return this.startAt;
    }

    public ExamEmployeeResultEntity startAt(Instant startAt) {
        this.setStartAt(startAt);
        return this;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getFinishedAt() {
        return this.finishedAt;
    }

    public ExamEmployeeResultEntity finishedAt(Instant finishedAt) {
        this.setFinishedAt(finishedAt);
        return this;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Long getNumberOfCorrect() {
        return this.numberOfCorrect;
    }

    public ExamEmployeeResultEntity numberOfCorrect(Long numberOfCorrect) {
        this.setNumberOfCorrect(numberOfCorrect);
        return this;
    }

    public void setNumberOfCorrect(Long numberOfCorrect) {
        this.numberOfCorrect = numberOfCorrect;
    }

    public Long getNumberOfQuestion() {
        return this.numberOfQuestion;
    }

    public ExamEmployeeResultEntity numberOfQuestion(Long numberOfQuestion) {
        this.setNumberOfQuestion(numberOfQuestion);
        return this;
    }

    public void setNumberOfQuestion(Long numberOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getMinPointToPass() {
        return minPointToPass;
    }

    public ExamEmployeeResultEntity minPointToPass(Long minPointToPass) {
        setMinPointToPass(minPointToPass);
        return this;
    }

    public void setMinPointToPass(Long minPointToPass) {
        this.minPointToPass = minPointToPass;
    }

    public boolean isPass() {
        return isPass;
    }

    public ExamEmployeeResultEntity pass(boolean isPass) {
        setIsPass(isPass);
        return this;
    }

    public void setIsPass(boolean pass) {
        isPass = pass;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamEmployeeResultEntity)) {
            return false;
        }
        return id != null && id.equals(((ExamEmployeeResultEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamEmployeeResultEntity{" +
            "id=" + getId() +
            ", rootId=" + getRootId() +
            ", startAt='" + getStartAt() + "'" +
            ", finishedAt='" + getFinishedAt() + "'" +
            ", numberOfCorrect=" + getNumberOfCorrect() +
            ", numberOfQuestion=" + getNumberOfQuestion() +
            "}";
    }
}
