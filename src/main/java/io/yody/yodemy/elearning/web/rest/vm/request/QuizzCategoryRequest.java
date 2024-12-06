package io.yody.yodemy.elearning.web.rest.vm.request;

import javax.validation.constraints.NotNull;

public class QuizzCategoryRequest {
     /**
     * id
     */
    private Long id;

    /**
     * Title
     */
    @NotNull
    private String title;

    /**
     * Mô tả
     */
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
