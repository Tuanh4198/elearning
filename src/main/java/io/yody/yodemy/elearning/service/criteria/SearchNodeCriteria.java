package io.yody.yodemy.elearning.service.criteria;

import java.io.Serializable;

public class SearchNodeCriteria implements Serializable {
    private Long rootId;
    private String type;
    private Integer page;
    private Integer limit;
    private String sortType;
    private String sortColumn;

    public SearchNodeCriteria(Long rootId, String type, Integer page, Integer limit, String sortType, String sortColumn) {
        this.rootId = rootId;
        this.type = type;
        this.page = page;
        this.limit = limit;
        this.sortType = sortType;
        this.sortColumn = sortColumn;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }
}
