package io.yody.yodemy.rule.langParser;

import io.yody.yodemy.rule.dslResolver.DSLKeywordResolver;
import io.yody.yodemy.rule.dslResolver.DSLResolver;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DSLParser {
    private DSLKeywordResolver keywordResolver;
    private DSLPatternUtil dslPatternUtil;

    public DSLParser(DSLKeywordResolver keywordResolver, DSLPatternUtil dslPatternUtil) {
        this.keywordResolver = keywordResolver;
        this.dslPatternUtil = dslPatternUtil;
    }

    public String resolveDomainSpecificKeywords(String expression){
        Map<String, Object> dslKeywordToResolverValueMap = executeDSLResolver(expression);
        return replaceKeywordsWithValue(expression, dslKeywordToResolverValueMap);
    }

    private Map<String, Object> executeDSLResolver(String expression) {
        List<String> listOfDslKeyword = dslPatternUtil.getListOfDslKeywords(expression);
        Map<String, Object> dslKeywordToResolverValueMap = new HashMap<>();
        listOfDslKeyword.stream()
            .forEach(
                dslKeyword -> {
                    String extractedDslKeyword = dslPatternUtil.extractKeyword(dslKeyword);
                    String keyResolver = dslPatternUtil.getKeywordResolver(extractedDslKeyword);
                    String keywordValue = dslPatternUtil.getKeywordValue(extractedDslKeyword);
                    DSLResolver resolver = keywordResolver.getResolver(keyResolver).get();
                    Object resolveValue = resolver.resolveValue(keywordValue);
                    dslKeywordToResolverValueMap.put(dslKeyword, resolveValue);
                }
            );
        return dslKeywordToResolverValueMap;
    }

    private String replaceKeywordsWithValue(String expression, Map<String, Object> dslKeywordToResolverValueMap){
        List<String> keyList = dslKeywordToResolverValueMap.keySet().stream().collect(Collectors.toList());
        for (int index = 0; index < keyList.size(); index++){
            String key = keyList.get(index);
            String dslResolveValue = dslKeywordToResolverValueMap.get(key).toString();
            expression = expression.replace(key, dslResolveValue);
        }
        return expression;
    }
}
