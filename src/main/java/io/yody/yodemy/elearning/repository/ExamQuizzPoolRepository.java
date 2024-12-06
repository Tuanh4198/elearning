package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.ExamQuizzPoolEntity;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExamQuizzPoolEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamQuizzPoolRepository extends JpaRepository<ExamQuizzPoolEntity, Long> {
    List<ExamQuizzPoolEntity> findAllByRootId(Long rootId);
    List<ExamQuizzPoolEntity> findAllByRootIdIn(List<Long> rootIds);
}
