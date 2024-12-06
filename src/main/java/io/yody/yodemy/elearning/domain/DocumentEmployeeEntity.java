package io.yody.yodemy.elearning.domain;

import io.yody.yodemy.elearning.domain.enumeration.DocumentEmployeeStatusEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nentangso.core.domain.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "document_employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DocumentEmployeeEntity extends AbstractAuditingEntity implements Serializable {
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
     * Id tài liệu gốc
     */
    @NotNull
    @Column(name = "root_id", nullable = false)
    private Long rootId;

    /**
     * status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DocumentEmployeeStatusEnum status = DocumentEmployeeStatusEnum.NOT_LEARNED;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public @NotNull Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public @NotNull @Size(min = 3, max = 255) String getCode() {
        return code;
    }

    public void setCode(@NotNull @Size(min = 3, max = 255) String code) {
        this.code = code;
    }

    public @NotNull @Size(max = 255) String getName() {
        return name;
    }

    public void setName(@NotNull @Size(max = 255) String name) {
        this.name = name;
    }

    public @NotNull Long getRootId() {
        return rootId;
    }

    public void setRootId(@NotNull Long rootId) {
        this.rootId = rootId;
    }

    public DocumentEmployeeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DocumentEmployeeStatusEnum status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
