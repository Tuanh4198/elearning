package io.yody.yodemy.elearning.service.specification;

import io.yody.yodemy.elearning.domain.NodeEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NodeSpecification implements Specification<NodeEntity> {
    private Long rootId;
    private String type;
    private String sortType;
    private String sortColumn;

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

    public NodeSpecification rootId(Long rootId) {
        setRootId(rootId);
        return this;
    }

    public NodeSpecification type(String type) {
        setType(type);
        return this;
    }

    public NodeSpecification sortType(String sortType) {
        setSortType(sortType);
        return this;
    }

    public NodeSpecification sortColumn(String sortColumn) {
        setSortColumn(sortColumn);
        return this;
    }

    @Override
    public Predicate toPredicate(Root<NodeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("deleted"), false));

        if (!Objects.isNull(rootId)) {
            predicates.add(criteriaBuilder.equal(root.get("rootId"), rootId));
        }

        if (!Objects.isNull(type)) {
            predicates.add(criteriaBuilder.equal(root.get("type"), type));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
