package io.yody.yodemy.elearning.domain;

import io.yody.yodemy.elearning.domain.enumeration.ExamEmployeeStatusEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.nentangso.core.domain.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Bài thi của từng user
 */
@Entity
@Table(name = "exam_employee")
@Where(clause = "deleted = false")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamEmployeeEntity extends AbstractAuditingEntity implements Serializable {

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
     * Mã nhân viên
     */
    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "code", length = 255, nullable = false)
    private String code;

    /**
     * Tên nhân viên
     */
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    /**
     * Id kỳ thi
     */
    @NotNull
    @Column(name = "root_id", nullable = false)
    private Long rootId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ExamEntity examEntity;

    /**
     * status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ExamEmployeeStatusEnum status = ExamEmployeeStatusEnum.NOT_ATTENDED;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExamEmployeeEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public ExamEmployeeEntity code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public ExamEmployeeEntity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRootId() {
        return this.rootId;
    }

    public ExamEmployeeEntity rootId(Long rootId) {
        this.setRootId(rootId);
        return this;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public ExamEmployeeStatusEnum getStatus() {
        return this.status;
    }

    public ExamEmployeeEntity status(ExamEmployeeStatusEnum status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ExamEmployeeStatusEnum status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public ExamEntity getExamEntity() {
        return examEntity;
    }

    public void setExamEntity(ExamEntity examEntity) {
        this.examEntity = examEntity;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamEmployeeEntity)) {
            return false;
        }
        return id != null && id.equals(((ExamEmployeeEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamEmployeeEntity{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", rootId=" + getRootId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
