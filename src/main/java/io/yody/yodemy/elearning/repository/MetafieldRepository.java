package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.domain.MetafieldEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MetafieldRepository extends JpaRepository<MetafieldEntity, Long> {
    @Modifying
    @Query("UPDATE MetafieldEntity m SET m.deleted = false WHERE m.ownerResource = :ownerResource AND m.ownerId = :ownerId")
    int deletedByOwnerResourceAndOwnerId(@Param("ownerResource") String ownerResource, @Param("ownerId") Long ownerId);

    List<MetafieldEntity> findAllByIdIn(List<Long> ids);
    List<MetafieldEntity> findAllByOwnerResourceAndOwnerId(String ownerResource, Long ownerId);
    List<MetafieldEntity> findAllByOwnerResourceAndOwnerIdIn(String ownerResource, List<Long> ownerId);
    List<MetafieldEntity> findAllByOwnerIdIn(List<Long> ownerIds);
}