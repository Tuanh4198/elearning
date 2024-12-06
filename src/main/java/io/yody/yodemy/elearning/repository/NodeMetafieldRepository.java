package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.NodeMetafieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeMetafieldRepository extends JpaRepository<NodeMetafieldEntity, Long>, JpaSpecificationExecutor<NodeMetafieldEntity> {
    void deleteByOwnerId(Long ownerId);
    List<NodeMetafieldEntity> findAllByOwnerResourceAndOwnerIdIn(String ownerResource, List<Long> ownerId);
    List<NodeMetafieldEntity> findAllByOwnerResourceAndOwnerId(String ownerResource, Long ownerId);

}
