package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.ExamEmployeeResultEntity;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExamEmployeeResultEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamEmployeeResultRepository extends JpaRepository<ExamEmployeeResultEntity, Long> {
    long countByRootId(Long rootId);
    List<ExamEmployeeResultEntity> findAllByRootId(Long rootId);
}
