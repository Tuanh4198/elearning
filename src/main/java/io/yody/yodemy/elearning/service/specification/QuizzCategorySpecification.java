package io.yody.yodemy.elearning.service.specification;

import io.yody.yodemy.elearning.domain.QuizzCategoryEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class QuizzCategorySpecification implements Specification<QuizzCategoryEntity> {

    private String search;

    public String getSearch() {
        return search;
    }

    public QuizzCategorySpecification search(String search) {
        setSearch(search);
        return this;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public Predicate toPredicate(Root<QuizzCategoryEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (!ObjectUtils.isEmpty(search)) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), '%' + search.toLowerCase() + '%'));
        }

        Order sortOrder = criteriaBuilder.desc(root.get("createdAt"));
        query.orderBy(sortOrder);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
