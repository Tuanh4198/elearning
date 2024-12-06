package io.yody.yodemy.elearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.yody.yodemy.elearning.domain.enumeration.CategoryTypeEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.yody.yodemy.elearning.domain.CategoryEntity} entity.
 */
@Schema(description = "Danh mục khóa học, kỳ thi")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoryDTO implements Serializable {

    /**
     * id
     */
    @Schema(description = "id", required = true)
    private Long id;

    /**
     * Title
     */
    @Size(min = 1)
    @Schema(description = "Title")
    private String title;

    @NotNull
    @Schema(description = "Domain")
    private CategoryTypeEnum type;

    private Long nodeId;

    /**
     * Description
     */
    @Schema(description = "Description")
    private String description;

    public CategoryDTO() {}

    // Private constructor to support builder methods
    private CategoryDTO(Long id, String title, CategoryTypeEnum type, String description) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
    }

    // Builder methods directly in CategoryDTO
    public CategoryDTO id(Long id) {
        this.id = id;
        return this;
    }

    public CategoryDTO title(String title) {
        this.title = title;
        return this;
    }

    public CategoryDTO type(CategoryTypeEnum type) {
        this.type = type;
        return this;
    }

    public CategoryDTO description(String description) {
        this.description = description;
        return this;
    }

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

    public CategoryTypeEnum getType() {
        return type;
    }

    public void setType(CategoryTypeEnum type) {
        this.type = type;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryDTO)) {
            return false;
        }

        CategoryDTO categoryDTO = (CategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", type=" + getType() +
            "}";
    }
}

