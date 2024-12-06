package io.yody.yodemy.elearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.yody.yodemy.elearning.domain.QuizzAnswerEntity} entity.
 */
@Schema(description = "Đáp án câu hỏi")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizzAnswerDTO implements Serializable {

    /**
     * id
     */
    @NotNull
    @Schema(description = "id", required = true)
    private Long id;

    /**
     * Quizz id
     */
    @Schema(description = "Quizz id")
    private Long rootId;

    /**
     * Tên đáp án
     */
    @Size(max = 255)
    @Schema(description = "Tên đáp án")
    private String title;

    /**
     * Nội dung đáp án
     */
    @NotNull
    @Schema(description = "Nội dung đáp án", required = true)
    private String content;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<MetafieldDTO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldDTO> metafields) {
        this.metafields = metafields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizzAnswerDTO)) {
            return false;
        }

        QuizzAnswerDTO quizzAnswerDTO = (QuizzAnswerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizzAnswerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizzAnswerDTO{" +
            "id=" + getId() +
            ", rootId=" + getRootId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
