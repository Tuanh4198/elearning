package io.yody.yodemy.elearning.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nentangso.core.domain.AbstractAuditingEntity;

/**
 * Pool câu hỏi
 */
@Entity
@Table(name = "exam_quizz_pool")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamQuizzPoolEntity extends AbstractAuditingEntity implements Serializable {

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
     * Id kỳ thi gốc
     */
    @NotNull
    @Column(name = "root_id", nullable = false)
    private Long rootId;

    /**
     * Id nguồn (Id danh mục câu hỏi hoặc id câu hỏi)
     */
    @NotNull
    @Column(name = "source_id", nullable = false)
    private Long sourceId;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExamQuizzPoolEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRootId() {
        return rootId;
    }

    public ExamQuizzPoolEntity rootId(Long rootId) {
        this.setRootId(rootId);
        return this;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Long getSourceId() {
        return this.sourceId;
    }

    public ExamQuizzPoolEntity sourceId(Long sourceId) {
        this.setSourceId(sourceId);
        return this;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
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
        if (!(o instanceof ExamQuizzPoolEntity)) {
            return false;
        }
        return id != null && id.equals(((ExamQuizzPoolEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamQuizzPoolEntity{" +
            "id=" + getId() +
            ", sourceId=" + getSourceId() +
            "}";
    }
}
