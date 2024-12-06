package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long>, JpaSpecificationExecutor<AttachmentEntity> {
}
