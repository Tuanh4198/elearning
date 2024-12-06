package io.yody.yodemy.elearning.web.rest.vm.request;

public class ExamEmployeeAnswerRequest {

    private Long quizzId;
    private String answer;

    public Long getQuizzId() {
        return quizzId;
    }

    public void setQuizzId(Long quizzId) {
        this.quizzId = quizzId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
