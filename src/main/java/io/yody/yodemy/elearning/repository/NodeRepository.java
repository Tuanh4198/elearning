package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<NodeEntity, Long>, JpaSpecificationExecutor<NodeEntity> {
    List<NodeEntity> findByRootId(Long rootId);
    List<NodeEntity> findByIdIn(List<Long> ids);
    Boolean existsByRootId(Long rootId);
    boolean existsByLabelAndRootId(String label, Long rootId);
    boolean existsByLabelAndRootIdAndIdNot(String label, Long rootId, Long id);

}
