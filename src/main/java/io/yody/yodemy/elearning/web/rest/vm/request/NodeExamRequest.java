package io.yody.yodemy.elearning.web.rest.vm.request;

public class NodeExamRequest {
    private NodeRequest node;
    private ExamRequest exam;

    public NodeRequest getNode() {
        return node;
    }

    public void setNode(NodeRequest node) {
        this.node = node;
    }

    public ExamRequest getExam() {
        return exam;
    }

    public void setExam(ExamRequest exam) {
        this.exam = exam;
    }
}
