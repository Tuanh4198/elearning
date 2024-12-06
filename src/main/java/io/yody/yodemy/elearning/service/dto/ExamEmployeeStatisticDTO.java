package io.yody.yodemy.elearning.service.dto;

import io.yody.yodemy.elearning.domain.enumeration.ExamEmployeeStatusEnum;
import java.io.Serializable;
import java.time.Instant;

public class ExamEmployeeStatisticDTO implements Serializable {

    private Long numberOfTest;
    private Long numberOfCorrect;
    private Long numberOfQuestion;
    private Instant startAt;
    private Instant finishedAt;
    private ExamEmployeeStatusEnum status = ExamEmployeeStatusEnum.NOT_ATTENDED;

    public Long getNumberOfTest() {
        return numberOfTest;
    }

    public ExamEmployeeStatisticDTO numberOfTest(Long numberOfTest) {
        setNumberOfTest(numberOfTest);
        return this;
    }

    public void setNumberOfTest(Long numberOfTest) {
        this.numberOfTest = numberOfTest;
    }

    public Long getNumberOfCorrect() {
        return numberOfCorrect;
    }

    public ExamEmployeeStatisticDTO numberOfCorrect(Long numberOfCorrect) {
        setNumberOfCorrect(numberOfCorrect);
        return this;
    }

    public void setNumberOfCorrect(Long numberOfCorrect) {
        this.numberOfCorrect = numberOfCorrect;
    }

    public Long getNumberOfQuestion() {
        return numberOfQuestion;
    }

    public ExamEmployeeStatisticDTO numberOfQuestion(Long numberOfQuestion) {
        setNumberOfQuestion(numberOfQuestion);
        return this;
    }

    public void setNumberOfQuestion(Long numberOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
    }

    public ExamEmployeeStatusEnum getStatus() {
        return status;
    }

    public ExamEmployeeStatisticDTO status(ExamEmployeeStatusEnum status) {
        setStatus(status);
        return this;
    }

    public void setStatus(ExamEmployeeStatusEnum status) {
        this.status = status;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public ExamEmployeeStatisticDTO startAt(Instant startAt) {
        setStartAt(startAt);
        return this;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public ExamEmployeeStatisticDTO finishedAt(Instant finishedAt) {
        setFinishedAt(finishedAt);
        return this;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }
}
