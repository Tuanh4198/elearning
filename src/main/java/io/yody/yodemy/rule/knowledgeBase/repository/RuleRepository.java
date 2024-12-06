package io.yody.yodemy.rule.knowledgeBase.repository;

import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<RuleEntity, Long> {
    @Query(value = "SELECT * FROM rule WHERE namespace = :namespace AND root_id = :rootId AND deleted = false", nativeQuery = true)
    List<RuleEntity> findByNamespaceAndRootId(@Param("namespace") String namespace, @Param("rootId") Long rootId);

    @Query(value = "SELECT * FROM rule WHERE namespace = :namespace AND root_id IN :rootIds AND deleted = false", nativeQuery = true)
    List<RuleEntity> findByNamespaceAndRootIdIn(@Param("namespace") String namespace, @Param("rootIds") List<Long> rootIds);

    @Query(value = "SELECT * FROM rule WHERE namespace IN :namespaces AND root_id IN :rootIds AND deleted = false", nativeQuery = true)
    List<RuleEntity> findByNamespaceInAndRootIdIn(@Param("namespaces") List<String> namespaces, @Param("rootIds") List<Long> rootIds);

    @Query(
        value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM rule WHERE namespace = :namespace AND root_id = :rootId AND deleted = false",
        nativeQuery = true
    )
    Boolean existsByNamespaceAndRootId(@Param("namespace") String namespace, @Param("rootId") Long rootId);

    @Query(value = "SELECT * FROM rule WHERE namespace = :namespace AND deleted = false", nativeQuery = true)
    List<RuleEntity> findByNamespace(@Param("namespace") String namespace);
}
