package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for the CourseEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long>, JpaSpecificationExecutor<CourseEntity> {
    @Query("SELECT c FROM CourseEntity c WHERE c.title = :title AND c.deleted = :deleted and c.id != :id")
    List<CourseEntity> findDifferentIdAndTitle(@Param("id") Long id, @Param("title") String title, @Param("deleted") boolean deleted);

    @Query("SELECT c FROM CourseEntity c WHERE c.title = :title AND c.deleted = :deleted")
    List<CourseEntity> findByTitle(@Param("title") String title, @Param("deleted") boolean deleted);

    List<CourseEntity> findAllByIdIn(List<Long> ids);

    List<CourseEntity> findByApplyTimeBetween(Instant from, Instant to);

    List<CourseEntity> findAllByNodeIdIn(List<Long> nodeIds);

    CourseEntity findByNodeId(Long nodeId);
}
