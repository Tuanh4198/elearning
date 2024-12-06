package io.yody.yodemy.elearning.service.specification;

import io.yody.yodemy.elearning.domain.DocumentEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class DocumentSpecification implements Specification<DocumentEntity> {

    private String search;

    private Long rootId;

    public String getSearch() {
        return search;
    }

    public Long getRootId() {
        return rootId;
    }

    public DocumentSpecification search(String search, Long rootId) {
        setSearch(search);
        setRootId(rootId);
        return this;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    @Override
    public Predicate toPredicate(Root<DocumentEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(search)) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + search.toLowerCase() + '%'));
        }

        if (!ObjectUtils.isEmpty(rootId)) {
            predicates.add(criteriaBuilder.equal(root.get("rootId"), rootId));
        }

        Order sortOrder = criteriaBuilder.desc(root.get("createdAt"));
        query.orderBy(sortOrder);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
