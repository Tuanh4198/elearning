package io.yody.yodemy.elearning.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nentangso.core.domain.AbstractAuditingEntity;

/**
 * NodeMetafield entity.
 */
@Entity
@Table(name = "node_metafields")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NodeMetafieldEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "owner_resource", nullable = false, length = 20)
    private String ownerResource;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @NotNull
    @Column(name = "namespace", nullable = false, length = 20)
    private String namespace;

    @NotNull
    @Column(name = "key", nullable = false, length = 30)
    private String key;

    @Column(name = "value")
    private String value;

    @NotNull
    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "created_by", nullable = false, length = 255, updatable = false)
    private String createdBy = "system";

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull String getOwnerResource() {
        return ownerResource;
    }

    public void setOwnerResource(@NotNull String ownerResource) {
        this.ownerResource = ownerResource;
    }

    public @NotNull Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(@NotNull Long ownerId) {
        this.ownerId = ownerId;
    }

    public @NotNull String getNamespace() {
        return namespace;
    }

    public void setNamespace(@NotNull String namespace) {
        this.namespace = namespace;
    }

    public @NotNull String getKey() {
        return key;
    }

    public void setKey(@NotNull String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public @NotNull String getType() {
        return type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(@NotNull boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

