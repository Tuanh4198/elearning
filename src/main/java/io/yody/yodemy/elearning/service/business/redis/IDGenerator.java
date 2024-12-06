package io.yody.yodemy.elearning.service.business.redis;

public interface IDGenerator {
    Long nextId(String key);
}
