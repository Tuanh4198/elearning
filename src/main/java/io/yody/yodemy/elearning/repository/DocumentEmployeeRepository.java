package io.yody.yodemy.elearning.repository;

import io.yody.yodemy.elearning.domain.DocumentEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentEmployeeRepository extends JpaRepository<DocumentEmployeeEntity, Long>, JpaSpecificationExecutor<DocumentEmployeeEntity> {
    List<DocumentEmployeeEntity> findAllByCodeAndRootId(String code, Long id);
    List<DocumentEmployeeEntity> findAllByCodeAndRootIdIn(String code, List<Long> id);
}
