package io.yody.yodemy.elearning.web.rest.vm.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.validation.constraints.NotNull;

public class ExamEmployeeSubmitRequest implements Serializable {

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

    @NotNull
    @Schema(description = "Danh sách đáp án")
    private List<ExamEmployeeAnswerRequest> answers;

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

    public List<ExamEmployeeAnswerRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<ExamEmployeeAnswerRequest> answers) {
        this.answers = answers;
    }
}
