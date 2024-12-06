package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the ExamEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, Long>, JpaSpecificationExecutor<ExamEntity> {
    ExamEntity findByNodeId(Long nodeId);
    List<ExamEntity> findAllByIdIn(List<Long> ids);
    List<ExamEntity> findAllByNodeIdIn(List<Long> nodeIds);

}
