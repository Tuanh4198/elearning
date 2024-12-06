package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.FlowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowRepository extends JpaRepository<FlowEntity, Long>, JpaSpecificationExecutor<FlowEntity> {
}
