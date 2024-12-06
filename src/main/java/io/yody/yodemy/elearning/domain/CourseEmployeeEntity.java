package io.yody.yodemy.elearning.domain;

import io.yody.yodemy.elearning.domain.enumeration.CourseEmployeeStatusEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nentangso.core.domain.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Khóa học giao cho user
 */
@Entity
@Table(name = "course_employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourseEmployeeEntity extends AbstractAuditingEntity implements Serializable {

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
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    /**
     * Id khóa học gốc
     */
    @NotNull
    @Column(name = "root_id", nullable = false)
    private Long rootId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CourseEntity courseEntity;

    /**
     * status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CourseEmployeeStatusEnum status = CourseEmployeeStatusEnum.NOT_ATTENDED;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CourseEmployeeEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public CourseEmployeeEntity code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public CourseEmployeeEntity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRootId() {
        return this.rootId;
    }

    public CourseEmployeeEntity rootId(Long rootId) {
        this.setRootId(rootId);
        return this;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public CourseEmployeeStatusEnum getStatus() {
        return this.status;
    }

    public CourseEmployeeEntity status(CourseEmployeeStatusEnum status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CourseEmployeeStatusEnum status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public CourseEntity getCourseEntity() {
        return courseEntity;
    }

    public void setCourseEntity(CourseEntity courseEntity) {
        this.courseEntity = courseEntity;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseEmployeeEntity)) {
            return false;
        }
        return id != null && id.equals(((CourseEmployeeEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseEmployeeEntity{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", rootId=" + getRootId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
