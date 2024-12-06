package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.DocumentEntity;
import io.yody.yodemy.elearning.domain.QuizzEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long>, JpaSpecificationExecutor<DocumentEntity> {
    List<DocumentEntity> findAllByRootIdIn(List<Long> rootIds);
    List<DocumentEntity> findAllByRootId(Long rootI);
    Page<DocumentEntity> findPageByRootId(Long rootId, Pageable pageable);
}
