package io.yody.yodemy.elearning.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface IDGeneratorRepository {
    Long getLastId(String key);
}
