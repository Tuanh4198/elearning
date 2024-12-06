package io.yody.yodemy.elearning.service.criteria;

import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.time.Instant;


@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourseEmployeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private Long categoryId;

    private String status;

    private Instant from;

    private Instant to;

    public CourseEmployeeCriteria() {}

    public CourseEmployeeCriteria(LongFilter id, StringFilter title, Long categoryId, String status, Instant from, Instant to) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.status = status;
        this.from = from;
        this.to = to;
    }

    public Instant getFrom() {
        return from;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public Instant getTo() {
        return to;
    }

    public void setTo(Instant to) {
        this.to = to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public Criteria copy() {
        return null;
    }
}
