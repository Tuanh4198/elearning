package io.yody.yodemy.elearning.service.specification;

import io.yody.yodemy.elearning.domain.QuizzEntity;
import io.yody.yodemy.elearning.domain.enumeration.QuizzTypeEnum;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class QuizzSpecification implements Specification<QuizzEntity> {

    private String search;

    private QuizzTypeEnum type;

    private Long categoryId;

    private List<Long> quizzIds;

    public String getSearch() {
        return search;
    }

    public QuizzTypeEnum getType() {
        return type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public List<Long> getQuizzIds() {
        return quizzIds;
    }

    public QuizzSpecification search(String search, QuizzTypeEnum type, Long categoryId, List<Long> quizzIds) {
        setSearch(search);
        setType(type);
        setCategoryId(categoryId);
        setQuizzIds(quizzIds);
        return this;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setType(QuizzTypeEnum type) {
        this.type = type;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setQuizzIds(List<Long> quizzIds) {
        this.quizzIds = quizzIds;
    }

    @Override
    public Predicate toPredicate(Root<QuizzEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(search)) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), '%' + search.toLowerCase() + '%'));
        }

        if (!ObjectUtils.isEmpty(type)) {
            predicates.add(criteriaBuilder.equal(root.get("type"), type));
        }

        if (!ObjectUtils.isEmpty(categoryId)) {
            predicates.add(criteriaBuilder.equal(root.get("categoryId"), categoryId));
        }

        if (!ObjectUtils.isEmpty(quizzIds)) {
            CriteriaBuilder.In<Long> inClause = criteriaBuilder.in(root.get("id"));
            for (Long id : quizzIds) {
                inClause.value(id);
            }
            predicates.add(inClause);
        }

        Order sortOrder = criteriaBuilder.desc(root.get("createdAt"));
        query.orderBy(sortOrder);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
