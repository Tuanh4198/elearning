package io.yody.yodemy.elearning.web.rest.vm.request;

import java.time.Instant;

public class LearnCourseRequest {

    Instant learnStartAt;
    Instant learnFinishAt;

    public Instant getLearnFinishAt() {
        return learnFinishAt;
    }

    public void setLearnFinishAt(Instant learnFinishAt) {
        this.learnFinishAt = learnFinishAt;
    }

    public Instant getLearnStartAt() {
        return learnStartAt;
    }

    public void setLearnStartAt(Instant learnStartAt) {
        this.learnStartAt = learnStartAt;
    }
}
