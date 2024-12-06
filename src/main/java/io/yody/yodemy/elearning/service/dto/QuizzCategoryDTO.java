package io.yody.yodemy.elearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.yody.yodemy.elearning.domain.QuizzCategoryEntity} entity.
 */
@Schema(description = "Danh mục câu hỏi")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizzCategoryDTO implements Serializable {

    /**
     * id
     */
    @Schema(description = "id", required = true)
    private Long id;

    /**
     * Title
     */
    @NotNull
    @Size(min = 1, max = 255)
    @Schema(description = "Title", required = true)
    private String title;

    /**
     * Mô tả
     */
    @Schema(description = "Mô tả")
    private String description;

    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizzCategoryDTO)) {
            return false;
        }

        QuizzCategoryDTO quizzCategoryDTO = (QuizzCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizzCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizzCategoryDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
