package io.yody.yodemy.elearning.service.criteria;

import java.io.Serializable;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private LongFilter categoryId;

    public ExamCriteria() {}

    public ExamCriteria(LongFilter id, StringFilter title, LongFilter categoryId) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
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

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public Criteria copy() {
        return null;
    }
}
