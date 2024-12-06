package io.yody.yodemy.elearning.web.rest.vm.request;

import io.yody.yodemy.elearning.domain.enumeration.QuizzTypeEnum;
import java.util.List;

public class QuizzSearchRequest {

    private String search;
    private QuizzTypeEnum type;
    private Long categoryId;
    private List<Long> quizzIds;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public QuizzTypeEnum getType() {
        return type;
    }

    public void setType(QuizzTypeEnum type) {
        this.type = type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getQuizzIds() {
        return quizzIds;
    }

    public void setQuizzIds(List<Long> quizzIds) {
        this.quizzIds = quizzIds;
    }
}
