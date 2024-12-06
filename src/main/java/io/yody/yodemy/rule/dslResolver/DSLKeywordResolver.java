package io.yody.yodemy.rule.dslResolver;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DSLKeywordResolver {
    Map<String, DSLResolver> dslKeywordResolverList;

    public DSLKeywordResolver() {
    }

    public DSLKeywordResolver(Map<String, DSLResolver> dslKeywordResolverList) {
        this.dslKeywordResolverList = dslKeywordResolverList;
    }

    public DSLKeywordResolver(List<DSLResolver> resolverList) {
        dslKeywordResolverList = resolverList.stream()
            .collect(Collectors.toMap(DSLResolver::getResolverKeyword, Function.identity()));
    }

    public Map<String, DSLResolver> getDslKeywordResolverList(){
        return dslKeywordResolverList;
    }

    public Optional<DSLResolver> getResolver(String keyword) {
        return Optional.ofNullable(dslKeywordResolverList.get(keyword));
    }
}
