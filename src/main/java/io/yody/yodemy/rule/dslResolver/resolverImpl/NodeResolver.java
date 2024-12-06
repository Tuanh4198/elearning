package io.yody.yodemy.rule.dslResolver.resolverImpl;

import io.yody.yodemy.rule.dslResolver.DSLResolver;
import org.springframework.stereotype.Component;

@Component
public class NodeResolver implements DSLResolver {
    private static final String RESOLVER_KEYWORD = "node";
    private static final String SAMPLE_KEYWORD = "sample_keyword";

    @Override
    public String getResolverKeyword() {
        return RESOLVER_KEYWORD;
    }

    @Override
    public Object resolveValue(String keyword) {
        if (keyword.equalsIgnoreCase(SAMPLE_KEYWORD)){
            return false;
        }

        return null;
    }
}
