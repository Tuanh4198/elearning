package io.yody.yodemy.elearning.service.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.yody.yodemy.elearning.domain.enumeration.ExamEmployeeStatusEnum;
import java.io.Serializable;
import java.time.Instant;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.filter.StringFilter;

@ParameterObject
public class ExamEmployeeCriteria implements Serializable {

    private static final long serialVersionUID = 1L;
    private ExamEmployeeStatusEnum status;
    private Long categoryId;
    private Instant from;
    private Instant to;
    private StringFilter title;
    private String search;

    @JsonProperty("root_id")
    private Long rootId;

    public ExamEmployeeCriteria() {}

    public ExamEmployeeCriteria(ExamEmployeeStatusEnum status, Long categoryId, Instant from, Instant to, StringFilter title) {
        this.status = status;
        this.categoryId = categoryId;
        this.from = from;
        this.to = to;
        this.title = title;
    }

    public ExamEmployeeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ExamEmployeeStatusEnum status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }
}
