package io.yody.yodemy.elearning.service.dto;

import java.io.Serializable;

public class GeneralStatisticDTO implements Serializable {

    private int totalCompletedCourse;
    private int totalCourse;
    private int totalCompletedExam;
    private int totalExam;

    public int getTotalCompletedCourse() {
        return totalCompletedCourse;
    }

    public void setTotalCompletedCourse(int totalCompletedCourse) {
        this.totalCompletedCourse = totalCompletedCourse;
    }

    public GeneralStatisticDTO totalCompletedCourse(int totalCompletedCourse) {
        setTotalCompletedCourse(totalCompletedCourse);
        return this;
    }

    public int getTotalCourse() {
        return totalCourse;
    }

    public void setTotalCourse(int totalCourse) {
        this.totalCourse = totalCourse;
    }

    public GeneralStatisticDTO totalCourse(int totalCourse) {
        setTotalCourse(totalCourse);
        return this;
    }

    public int getTotalCompletedExam() {
        return totalCompletedExam;
    }

    public void setTotalCompletedExam(int totalCompletedExam) {
        this.totalCompletedExam = totalCompletedExam;
    }

    public GeneralStatisticDTO totalCompletedExam(int totalCompletedExam) {
        setTotalCompletedExam(totalCompletedExam);
        return this;
    }

    public int getTotalExam() {
        return totalExam;
    }

    public void setTotalExam(int totalExam) {
        this.totalExam = totalExam;
    }

    public GeneralStatisticDTO totalExam(int totalExam) {
        setTotalExam(totalExam);
        return this;
    }
}
