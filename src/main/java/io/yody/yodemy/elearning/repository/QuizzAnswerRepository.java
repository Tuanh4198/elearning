package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.QuizzAnswerEntity;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the QuizzAnswerEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizzAnswerRepository extends JpaRepository<QuizzAnswerEntity, Long> {
    @Modifying
    @Transactional
    @Query("update from QuizzAnswerEntity p set p.deleted = true where p.rootId = ?1")
    void deleteByRootId(Long rootId);

    List<QuizzAnswerEntity> findAllByRootId(Long rootId);
    List<QuizzAnswerEntity> findAllByRootIdIn(List<Long> rootIds);
}
