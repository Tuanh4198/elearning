package io.yody.yodemy.elearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.yody.yodemy.elearning.domain.ExamQuizzPoolEntity} entity.
 */
@Schema(description = "Pool câu hỏi")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamQuizzPoolDTO implements Serializable {

    /**
     * id
     */
    @NotNull
    @Schema(description = "id", required = true)
    private Long id;

    /**
     * Id kỳ thi gốc
     */
    @NotNull
    @Schema(description = "Id kỳ thi gốc", required = true)
    private Long rootId;

    /**
     * Id nguồn (Id danh mục câu hỏi hoặc id câu hỏi)
     */
    @NotNull
    @Schema(description = "Id nguồn (Id danh mục câu hỏi hoặc id câu hỏi)", required = true)
    private Long sourceId;

    private Long categoryId;
    private String categoryName;

    private List<MetafieldDTO> metafields;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public List<MetafieldDTO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldDTO> metafields) {
        this.metafields = metafields;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamQuizzPoolDTO)) {
            return false;
        }

        ExamQuizzPoolDTO examQuizzPoolDTO = (ExamQuizzPoolDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examQuizzPoolDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamQuizzPoolDTO{" +
            "id=" + getId() +
            ", rootId=" + getRootId() +
            ", sourceId=" + getSourceId() +
            "}";
    }
}
