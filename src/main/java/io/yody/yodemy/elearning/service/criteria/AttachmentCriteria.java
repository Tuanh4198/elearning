package io.yody.yodemy.elearning.service.criteria;

import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

@ParameterObject
public class AttachmentCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter source;

    private StringFilter url;

    private LongFilter rootId;

    private StringFilter attachmentType;

    private StringFilter attachmentName;

    private StringFilter createdBy;

    private InstantFilter createdAt;

    private StringFilter updatedBy;

    private InstantFilter updatedAt;

    private Boolean distinct;

    public AttachmentCriteria() {}

    public AttachmentCriteria(AttachmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.source = other.source == null ? null : other.source.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.rootId = other.rootId == null ? null : other.rootId.copy();
        this.attachmentType = other.attachmentType == null ? null : other.attachmentType.copy();
        this.attachmentName = other.attachmentName == null ? null : other.attachmentName.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedBy = other.updatedBy == null ? null : other.updatedBy.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AttachmentCriteria copy() {
        return new AttachmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSource() {
        return source;
    }

    public StringFilter source() {
        if (source == null) {
            source = new StringFilter();
        }
        return source;
    }

    public void setSource(StringFilter source) {
        this.source = source;
    }

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public LongFilter getRootId() {
        return rootId;
    }

    public LongFilter rootId() {
        if (rootId == null) {
            rootId = new LongFilter();
        }
        return rootId;
    }

    public void setRootId(LongFilter rootId) {
        this.rootId = rootId;
    }

    public StringFilter getAttachmentType() {
        return attachmentType;
    }

    public StringFilter attachmentType() {
        if (attachmentType == null) {
            attachmentType = new StringFilter();
        }
        return attachmentType;
    }

    public void setAttachmentType(StringFilter attachmentType) {
        this.attachmentType = attachmentType;
    }

    public StringFilter getAttachmentName() {
        return attachmentName;
    }

    public StringFilter attachmentName() {
        if (attachmentName == null) {
            attachmentName = new StringFilter();
        }
        return attachmentName;
    }

    public void setAttachmentName(StringFilter attachmentName) {
        this.attachmentName = attachmentName;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            createdAt = new InstantFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public StringFilter getUpdatedBy() {
        return updatedBy;
    }

    public StringFilter updatedBy() {
        if (updatedBy == null) {
            updatedBy = new StringFilter();
        }
        return updatedBy;
    }

    public void setUpdatedBy(StringFilter updatedBy) {
        this.updatedBy = updatedBy;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new InstantFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AttachmentCriteria that = (AttachmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
                Objects.equals(source, that.source) &&
                Objects.equals(url, that.url) &&
                Objects.equals(rootId, that.rootId) &&
                Objects.equals(attachmentType, that.attachmentType) &&
                Objects.equals(attachmentName, that.attachmentName) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, url, rootId, attachmentType, attachmentName, createdBy, createdAt, updatedBy, updatedAt, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (source != null ? "source=" + source + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (rootId != null ? "rootId=" + rootId + ", " : "") +
            (attachmentType != null ? "attachmentType=" + attachmentType + ", " : "") +
            (attachmentName != null ? "attachmentName=" + attachmentName + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
