package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.ExamEmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the ExamEmployeeEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamEmployeeRepository extends JpaRepository<ExamEmployeeEntity, Long>, JpaSpecificationExecutor<ExamEmployeeEntity> {
    List<ExamEmployeeEntity> findAllByRootIdAndCodeIn(Long rootId, List<String> codes);
    List<ExamEmployeeEntity> findAllByRootId(Long rootId);
    Page<ExamEmployeeEntity> findAllByCode(String code, Pageable pageable);
    List<ExamEmployeeEntity> findByCodeAndRootId(String code, Long rootId);
    List<ExamEmployeeEntity> findAllByCode(String code);
    List<ExamEmployeeEntity> findAllByCodeAndRootIdIn(String code, List<Long> rootIds);

    @Query(
        value = "SELECT COUNT(*) FROM exam_employee ee " +
        "JOIN exam e ON ee.root_id = e.id " +
        "WHERE ee.code = :userCode AND ee.deleted = false AND e.deleted = false",
        nativeQuery = true
    )
    int countTotalExamEmployee(@Param("userCode") String userCode);

    @Query(
        value = "SELECT COUNT(*) FROM exam_employee ee " +
        "JOIN exam e ON ee.root_id = e.id " +
        "WHERE ee.code = :userCode AND ee.deleted = false AND e.deleted = false AND ee.status = 'PASS'",
        nativeQuery = true
    )
    int countPassedExamEmployee(@Param("userCode") String userCode);
}
