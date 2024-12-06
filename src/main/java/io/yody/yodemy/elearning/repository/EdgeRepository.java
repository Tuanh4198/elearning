package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.EdgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EdgeRepository extends JpaRepository<EdgeEntity, Long>, JpaSpecificationExecutor<EdgeEntity> {
    List<EdgeEntity> findBySource(Long id);
    List<EdgeEntity> findByTarget(Long id);
}
