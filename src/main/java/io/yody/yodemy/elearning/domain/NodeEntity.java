package io.yody.yodemy.elearning.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.nentangso.core.domain.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Node entity.
 */
@Entity
@Table(name = "node")
@Where(clause = "deleted = false")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NodeEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "flow_id", nullable = false)
    private Long flowId;

    @Column(name = "root_id")
    private Long rootId;

    @Column(name = "type", length = 255)
    private String type;

    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "label", length = 255)
    private String label;

    @Column(name = "position_x", precision = 10, scale = 2)
    private BigDecimal positionX;

    @Column(name = "position_y", precision = 10, scale = 2)
    private BigDecimal positionY;

    @Column(name = "width", precision = 10, scale = 2)
    private BigDecimal width;

    @Column(name = "height", precision = 10, scale = 2)
    private BigDecimal height;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "thumb_url")
    private String thumbUrl;

    public NodeEntity() {
    }

    public NodeEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Long getFlowId() {
        return flowId;
    }

    public void setFlowId(@NotNull Long flowId) {
        this.flowId = flowId;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getPositionX() {
        return positionX;
    }

    public void setPositionX(BigDecimal positionX) {
        this.positionX = positionX;
    }

    public BigDecimal getPositionY() {
        return positionY;
    }

    public void setPositionY(BigDecimal positionY) {
        this.positionY = positionY;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}

