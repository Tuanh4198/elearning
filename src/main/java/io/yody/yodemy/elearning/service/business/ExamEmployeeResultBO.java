package io.yody.yodemy.elearning.service.business;

import java.time.Instant;

public class ExamEmployeeResultBO {

    private Long id;
    private Long rootId;
    private Instant startAt;
    private Instant finishedAt;
    private Long numberOfCorrect;
    private Long numberOfQuestion;

    public ExamEmployeeResultBO(Long id, Long rootId, Instant startAt, Instant finishedAt, Long numberOfCorrect, Long numberOfQuestion) {
        this.id = id;
        this.rootId = rootId;
        this.startAt = startAt;
        this.finishedAt = finishedAt;
        this.numberOfCorrect = numberOfCorrect;
        this.numberOfQuestion = numberOfQuestion;
    }

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
}
