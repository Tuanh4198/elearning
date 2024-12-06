package io.yody.yodemy.elearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.yody.yodemy.elearning.domain.ExamEmployeeResultEntity} entity.
 */
@Schema(description = "Kết quả bài thi của user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamEmployeeResultDTO implements Serializable {

    /**
     * id
     */
    @NotNull
    @Schema(description = "id", required = true)
    private Long id;

    /**
     * Id bài kiểm tra
     */
    @NotNull
    @Schema(description = "Id bài kiểm tra", required = true)
    private Long rootId;

    /**
     * Thời gian bắt đầu làm bài
     */
    @NotNull
    @Schema(description = "Thời gian bắt đầu làm bài", required = true)
    private Instant startAt;

    /**
     * Thời gian nộp bài
     */
    @NotNull
    @Schema(description = "Thời gian nộp bài", required = true)
    private Instant finishedAt;

    /**
     * Tổng số câu đúng
     */
    @NotNull
    @Schema(description = "Tổng số câu đúng", required = true)
    private Long numberOfCorrect;

    /**
     * numberOfQuestion
     */
    @NotNull
    @Schema(description = "numberOfQuestion", required = true)
    private Long numberOfQuestion;

    private Long minPointToPass;
    private boolean isPass;

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

    public Instant getStartAt() {
        return startAt;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Long getNumberOfCorrect() {
        return numberOfCorrect;
    }

    public void setNumberOfCorrect(Long numberOfCorrect) {
        this.numberOfCorrect = numberOfCorrect;
    }

    public Long getNumberOfQuestion() {
        return numberOfQuestion;
    }

    public void setNumberOfQuestion(Long numberOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
    }

    public Long getMinPointToPass() {
        return minPointToPass;
    }

    public void setMinPointToPass(Long minPointToPass) {
        this.minPointToPass = minPointToPass;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamEmployeeResultDTO)) {
            return false;
        }

        ExamEmployeeResultDTO examEmployeeResultDTO = (ExamEmployeeResultDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examEmployeeResultDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamEmployeeResultDTO{" +
            "id=" + getId() +
            ", rootId=" + getRootId() +
            ", startAt='" + getStartAt() + "'" +
            ", finishedAt='" + getFinishedAt() + "'" +
            ", numberOfCorrect=" + getNumberOfCorrect() +
            ", numberOfQuestion=" + getNumberOfQuestion() +
            "}";
    }
}
