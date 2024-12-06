package io.yody.yodemy.elearning.service.criteria;

import io.yody.yodemy.elearning.domain.enumeration.CategoryTypeEnum;
import java.io.Serializable;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.StringFilter;

@ParameterObject
public class CategoryCriteria implements Serializable, Criteria {

    private CategoryTypeEnum type;

    public CategoryCriteria() {}

    public CategoryCriteria(CategoryTypeEnum type) {
        this.type = type;
    }

    public CategoryTypeEnum getType() {
        return type;
    }

    public void setType(CategoryTypeEnum type) {
        this.type = type;
    }

    @Override
    public Criteria copy() {
        return null;
    }
}
