package io.yody.yodemy.elearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class AttachmentDTO implements Serializable {

    private Long id;

    /**
     * source of attachment
     */
    @NotNull
    @Schema(description = "source of attachment", required = true)
    private String source;

    /**
     * url of attachment
     */
    @NotNull
    @Schema(description = "url of attachment", required = true)
    private String url;

    private String thumbUrl;

    /**
     * attachment type
     */
    @NotNull
    @Schema(description = "attachment type", required = true)
    private String attachmentType;

    /**
     * attachment name
     */
    @NotNull
    @Schema(description = "attachment name", required = true)
    private String attachmentName;

    /**
     * The date and time when the entity was added.
     */
    @Schema(description = "The date and time when the entity was added.")
    private Instant createdAt;

    /**
     * The date and time when the entity was last updated.
     */
    @Schema(description = "The date and time when the entity was last updated.")
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttachmentDTO)) {
            return false;
        }

        AttachmentDTO attachmentDTO = (AttachmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attachmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentDTO{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", url='" + getUrl() + "'" +
            ", attachmentType='" + getAttachmentType() + "'" +
            ", attachmentName='" + getAttachmentName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
