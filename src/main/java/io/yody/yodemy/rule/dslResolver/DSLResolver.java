package io.yody.yodemy.rule.dslResolver;


public interface DSLResolver {
    String getResolverKeyword();
    Object resolveValue(String keyword);
}
