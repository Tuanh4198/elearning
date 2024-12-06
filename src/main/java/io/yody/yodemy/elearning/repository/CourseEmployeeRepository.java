package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.CourseEmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the CourseEmployeeEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseEmployeeRepository
    extends JpaRepository<CourseEmployeeEntity, Long>, JpaSpecificationExecutor<CourseEmployeeEntity> {
    List<CourseEmployeeEntity> findAllByRootIdAndCodeIn(Long rootId, List<String> codes);
    List<CourseEmployeeEntity> findAllByRootId(Long rootId);

    Page<CourseEmployeeEntity> findAllByCode(String code, Pageable pageable);

    List<CourseEmployeeEntity> findByCodeAndRootId(String code, Long rootId);

    List<CourseEmployeeEntity> findAllByCodeAndRootIdIn(String code, List<Long> rootIds);
    List<CourseEmployeeEntity> findAllByCode(String code);

    @Query(
        value = "SELECT COUNT(*) FROM course_employee ce " +
        "JOIN course c ON ce.root_id = c.id " +
        "WHERE ce.code = :userCode AND ce.deleted = false AND c.deleted = false",
        nativeQuery = true
    )
    int countTotalCourseEmployee(@Param("userCode") String userCode);

    @Query(
        value = "SELECT COUNT(*) FROM course_employee ce " +
        "JOIN course c ON ce.root_id = c.id " +
        "WHERE ce.code = :userCode AND ce.deleted = false AND c.deleted = false AND ce.status = 'LEARNED'",
        nativeQuery = true
    )
    int countLearnedCourseEmployee(@Param("userCode") String userCode);
}
