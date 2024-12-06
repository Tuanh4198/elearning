package io.yody.yodemy.elearning.service.specification;

import io.yody.yodemy.elearning.domain.CourseEmployeeEntity;
import io.yody.yodemy.elearning.domain.CourseEntity;
import io.yody.yodemy.elearning.domain.enumeration.CourseStatusEnum;
import org.springframework.data.jpa.domain.Specification;
import tech.jhipster.service.filter.StringFilter;

import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseEmployeeSpecification implements Specification<CourseEmployeeEntity> {

    private String code;
    private String status;
    private Long categoryId;
    private Instant from;
    private Instant to;
    private StringFilter title;

    public CourseEmployeeSpecification from(Instant from) {
        setFrom(from);
        return this;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public Instant getFrom() {
        return from;
    }

    public CourseEmployeeSpecification to(Instant to) {
        setTo(to);
        return this;
    }

    public void setTo(Instant to) {
        this.to = to;
    }

    public Instant getTo() {
        return to;
    }

    public CourseEmployeeSpecification categoryId(Long categoryId) {
        setCategoryId(categoryId);
        return this;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCode() {
        return code;
    }

    public CourseEmployeeSpecification code(String code) {
        setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public CourseEmployeeSpecification status(String status) {
        setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getTitle() {
        return title;
    }

    public CourseEmployeeSpecification title(StringFilter title) {
        setTitle(title);
        return this;
    }


    @Override
    public Specification<CourseEmployeeEntity> and(Specification<CourseEmployeeEntity> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<CourseEmployeeEntity> or(Specification<CourseEmployeeEntity> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<CourseEmployeeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Join<CourseEmployeeEntity, CourseEntity> courseEmployeeJoin = root.join("courseEntity", JoinType.INNER);
        predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
        predicates.add(criteriaBuilder.equal(courseEmployeeJoin.get("deleted"), false));
        if (!Objects.isNull(code)) {
            predicates.add(criteriaBuilder.equal(root.get("code"), code));
        }
        if (!Objects.isNull(title) && title.getContains() != null) {
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(courseEmployeeJoin.get("title")),
                "%" + title.getContains().toLowerCase() + "%"
                )
            );
        }
        if (!Objects.isNull(status)) {
            if (Objects.equals(status.toLowerCase(), CourseStatusEnum.IN_PROGRESS.name().toLowerCase())) {
                Instant now = Instant.now();
                Predicate applyTimePredicate = criteriaBuilder.lessThanOrEqualTo(courseEmployeeJoin.get("applyTime"), now);
                Predicate expireTimePredicate = criteriaBuilder.greaterThanOrEqualTo(courseEmployeeJoin.get("expireTime"), now);
                predicates.add(criteriaBuilder.and(applyTimePredicate, expireTimePredicate));
            } else {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
        }
        if (categoryId != null) {
            predicates.add(criteriaBuilder.equal(courseEmployeeJoin.get("categoryId"), categoryId));
        }
        if (!Objects.isNull(from) && !Objects.isNull(to)) {
            List<Predicate> overlapPredicates = new ArrayList<>();

            overlapPredicates.add(criteriaBuilder.lessThan(courseEmployeeJoin.get("applyTime"), to));

            Predicate expireTimeAfterFrom = criteriaBuilder.greaterThan(courseEmployeeJoin.get("expireTime"), from);
            Predicate expireTimeIsNull = criteriaBuilder.isNull(courseEmployeeJoin.get("expireTime"));
            Predicate expireTimePredicate = criteriaBuilder.or(expireTimeAfterFrom, expireTimeIsNull);

            overlapPredicates.add(expireTimePredicate);

            Predicate overlapPredicate = criteriaBuilder.and(overlapPredicates.toArray(new Predicate[0]));

            return criteriaBuilder.and(criteriaBuilder.and(predicates.toArray(new Predicate[0])), overlapPredicate);
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
