package io.yody.yodemy.elearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.yody.yodemy.elearning.domain.enumeration.QuizzTypeEnum;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link io.yody.yodemy.elearning.domain.QuizzEntity} entity.
 */
@Schema(description = "Câu hỏi")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizzDTO implements Serializable {

    @Schema(description = "metafields")
    List<MetafieldDTO> metafields;

    /**
     * Id
     */
    @Schema(description = "Id", required = true)
    private Long id;

    /**
     * Nội dung
     */
    @NotNull
    @Size(min = 1)
    @Schema(description = "Nội dung", required = true)
    private String content;

    /**
     * Id danh mục
     */
    @Schema(description = "Id danh mục")
    private Long categoryId;

    private String category;

    /**
     * Loại câu hỏi
     */
    @Schema(description = "Loại câu hỏi")
    private QuizzTypeEnum type;

    @Schema(description = "Câu trả lời")
    private List<QuizzAnswerDTO> answers;

    public List<MetafieldDTO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldDTO> metafields) {
        this.metafields = metafields;
    }

    public List<QuizzAnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizzAnswerDTO> answers) {
        this.answers = answers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public QuizzTypeEnum getType() {
        return type;
    }

    public void setType(QuizzTypeEnum type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizzDTO)) {
            return false;
        }

        QuizzDTO quizzDTO = (QuizzDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizzDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizzDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", categoryId=" + getCategoryId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
