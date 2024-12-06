package io.yody.yodemy.elearning.service.specification;

import io.yody.yodemy.elearning.domain.ExamEmployeeEntity;
import io.yody.yodemy.elearning.domain.ExamEntity;
import io.yody.yodemy.elearning.domain.enumeration.ExamEmployeeStatusEnum;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import tech.jhipster.service.filter.StringFilter;

public class ExamEmployeeSpecification implements Specification<ExamEmployeeEntity> {

    private String code;
    private ExamEmployeeStatusEnum status;
    private Long categoryId;
    private Instant from;
    private Instant to;
    private StringFilter title;
    private Long rootId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ExamEmployeeSpecification code(String code) {
        setCode(code);
        return this;
    }

    public ExamEmployeeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ExamEmployeeStatusEnum status) {
        this.status = status;
    }

    public ExamEmployeeSpecification status(ExamEmployeeStatusEnum status) {
        setStatus(status);
        return this;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public ExamEmployeeSpecification categoryId(Long categoryId) {
        setCategoryId(categoryId);
        return this;
    }

    public Instant getFrom() {
        return from;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public ExamEmployeeSpecification from(Instant from) {
        setFrom(from);
        return this;
    }

    public Instant getTo() {
        return to;
    }

    public void setTo(Instant to) {
        this.to = to;
    }

    public ExamEmployeeSpecification to(Instant to) {
        setTo(to);
        return this;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public ExamEmployeeSpecification title(StringFilter title) {
        setTitle(title);
        return this;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public ExamEmployeeSpecification rootId(Long rootId) {
        setRootId(rootId);
        return this;
    }

    @Override
    public Specification<ExamEmployeeEntity> and(Specification<ExamEmployeeEntity> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<ExamEmployeeEntity> or(Specification<ExamEmployeeEntity> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<ExamEmployeeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Join<ExamEmployeeEntity, ExamEntity> examEmployeeJoin = root.join("examEntity", JoinType.INNER);
        predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
        predicates.add(criteriaBuilder.equal(examEmployeeJoin.get("deleted"), false));

        if (!Objects.isNull(code)) {
            predicates.add(criteriaBuilder.equal(root.get("code"), code));
        }
        if (!Objects.isNull(rootId)) {
            predicates.add(criteriaBuilder.equal(examEmployeeJoin.get("id"), rootId));
        }
        if (!Objects.isNull(title) && title.getContains() != null) {
            predicates.add(
                criteriaBuilder.like(criteriaBuilder.lower(examEmployeeJoin.get("title")), "%" + title.getContains().toLowerCase() + "%")
            );
        }
        if (!Objects.isNull(status)) {
            Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), status);
            Predicate timePredicate = null;
            if (status.equals(ExamEmployeeStatusEnum.EXPIRED)) {
                statusPredicate = criteriaBuilder.notEqual(root.get("status"), ExamEmployeeStatusEnum.PASS);
                timePredicate = criteriaBuilder.lessThan(examEmployeeJoin.get("expireTime"), Instant.now());
            } else if (!status.equals(ExamEmployeeStatusEnum.PASS)) {
                timePredicate = criteriaBuilder.greaterThanOrEqualTo(examEmployeeJoin.get("expireTime"), Instant.now());
            }

            if (status.equals(ExamEmployeeStatusEnum.NOT_COMPLETED)) {
                ArrayList<ExamEmployeeStatusEnum> listNotCompletedStatus = new ArrayList<>(
                    List.of(ExamEmployeeStatusEnum.NOT_ATTENDED, ExamEmployeeStatusEnum.NOT_PASS)
                );
                statusPredicate = criteriaBuilder.in(root.get("status")).value(listNotCompletedStatus);
            }

            if (!Objects.isNull(timePredicate)) {
                predicates.add(criteriaBuilder.and(statusPredicate, timePredicate));
            } else {
                predicates.add(statusPredicate);
            }
        }

        if (!Objects.isNull(categoryId)) {
            predicates.add(criteriaBuilder.equal(examEmployeeJoin.get("categoryId"), categoryId));
        }

        if (!Objects.isNull(from) && !Objects.isNull(to)) {
            List<Predicate> overlapPredicates = new ArrayList<>();

            overlapPredicates.add(criteriaBuilder.lessThan(examEmployeeJoin.get("applyTime"), to));

            Predicate expireTimeAfterFrom = criteriaBuilder.greaterThan(examEmployeeJoin.get("expireTime"), from);
            Predicate expireTimeIsNull = criteriaBuilder.isNull(examEmployeeJoin.get("expireTime"));
            Predicate expireTimePredicate = criteriaBuilder.or(expireTimeAfterFrom, expireTimeIsNull);

            overlapPredicates.add(expireTimePredicate);

            Predicate overlapPredicate = criteriaBuilder.and(overlapPredicates.toArray(new Predicate[0]));

            return criteriaBuilder.and(criteriaBuilder.and(predicates.toArray(new Predicate[0])), overlapPredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
