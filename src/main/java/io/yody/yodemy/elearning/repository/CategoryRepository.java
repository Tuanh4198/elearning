package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.CategoryEntity;
import io.yody.yodemy.elearning.domain.enumeration.CategoryTypeEnum;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CategoryEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>, JpaSpecificationExecutor<CategoryEntity> {
    boolean existsByTitleAndTypeAndIdNot(String title, CategoryTypeEnum type, Long id);

    boolean existsByTitleAndType(String title, CategoryTypeEnum type);
    List<CategoryEntity> findAllByIdIn(List<Long> categoryIds);
}
