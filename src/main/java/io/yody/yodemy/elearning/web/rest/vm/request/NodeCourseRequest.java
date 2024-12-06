package io.yody.yodemy.elearning.web.rest.vm.request;

public class NodeCourseRequest {
    private NodeRequest node;
    private CourseRequest course;

    public NodeRequest getNode() {
        return node;
    }

    public void setNode(NodeRequest node) {
        this.node = node;
    }

    public CourseRequest getCourse() {
        return course;
    }

    public void setCourse(CourseRequest course) {
        this.course = course;
    }
}
