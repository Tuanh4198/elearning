package io.yody.yodemy.elearning.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.nentangso.core.domain.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Edge entity.
 */
@Entity
@Table(name = "edge")
@Where(clause = "deleted = false")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EdgeEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "source", nullable = false)
    private Long source;

    @NotNull
    @Column(name = "target", nullable = false)
    private Long target;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Long getSource() {
        return source;
    }

    public void setSource(@NotNull Long source) {
        this.source = source;
    }

    public @NotNull Long getTarget() {
        return target;
    }

    public void setTarget(@NotNull Long target) {
        this.target = target;
    }

    public @NotNull String getType() {
        return type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
