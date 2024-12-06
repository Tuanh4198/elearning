package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.QuizzCategoryEntity;
import io.yody.yodemy.elearning.domain.QuizzEntity;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuizzEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizzRepository extends JpaRepository<QuizzEntity, Long>, JpaSpecificationExecutor<QuizzEntity> {
    @Modifying
    @Transactional
    @Query("update from QuizzEntity p set p.deleted = true where p.id = ?1")
    void deleteById(Long id);

    List<QuizzEntity> findAllByIdIn(List<Long> ids);

    List<QuizzEntity> findAllByCategoryIdIn(List<Long> categoryIds);
}
