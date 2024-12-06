package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.QuizzCategoryEntity;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuizzCategoryEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizzCategoryRepository extends JpaRepository<QuizzCategoryEntity, Long>, JpaSpecificationExecutor<QuizzCategoryEntity> {
    @Modifying
    @Transactional
    @Query("update from QuizzCategoryEntity p set p.deleted = true where p.id = ?1")
    void deleteById(Long id);

    @Query("SELECT q FROM QuizzCategoryEntity q WHERE q.title = :title AND q.deleted = :deleted")
    List<QuizzCategoryEntity> findByTitle(@Param("title") String title, @Param("deleted") boolean deleted);

    @Query("SELECT q FROM QuizzCategoryEntity q WHERE q.title = :title AND q.deleted = :deleted and q.id != :id")
    List<QuizzCategoryEntity> findDifferentIdAndTitle(
        @Param("id") Long id,
        @Param("title") String title,
        @Param("deleted") boolean deleted
    );

    @Query("SELECT q FROM QuizzCategoryEntity q WHERE q.deleted = false")
    Page<QuizzCategoryEntity> findAllWithConditions(Pageable pageable);

    List<QuizzCategoryEntity> findAllByIdIn(List<Long> ids);
}
