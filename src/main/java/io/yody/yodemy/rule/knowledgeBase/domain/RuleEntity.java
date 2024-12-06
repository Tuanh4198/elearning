package io.yody.yodemy.rule.knowledgeBase.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.nentangso.core.domain.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "rule")
@Where(clause = "deleted = false")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RuleEntity extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;
    @NotNull
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "namespace")
    @Enumerated(EnumType.STRING)
    private RuleNamespace namespace;

    @Column(name = "condition")
    private String condition;

    @Column(name = "action")
    private String action;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "description")
    private String description;

    @Column(name = "root_id")
    private Long rootId;

    @Column(name = "deleted")
    private Boolean deleted = false;

    public RuleEntity() {
    }

    public RuleEntity(Long id) {
        this.id = id;
    }

    public @NotNull Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public RuleNamespace getNamespace() {
        return namespace;
    }

    public void setNamespace(RuleNamespace namespace) {
        this.namespace = namespace;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
