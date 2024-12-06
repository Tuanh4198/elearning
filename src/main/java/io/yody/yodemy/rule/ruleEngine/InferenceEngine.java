package io.yody.yodemy.rule.ruleEngine;

import io.yody.yodemy.rule.knowledgeBase.domain.RuleNamespace;
import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;
import io.yody.yodemy.rule.langParser.RuleParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public abstract class InferenceEngine<INPUT_DATA, OUTPUT_RESULT> {
    private final RuleParser<INPUT_DATA, OUTPUT_RESULT> ruleParser;

    public InferenceEngine(RuleParser<INPUT_DATA, OUTPUT_RESULT> ruleParser) {
        this.ruleParser = ruleParser;
    }

    /**
     * Run inference engine on set of rules for given data.
     * @param listOfRules
     * @param inputData
     * @return
     */
    public OUTPUT_RESULT run (List<RuleDTO> listOfRules, INPUT_DATA inputData){
        if (null == listOfRules || listOfRules.isEmpty()){
            return null;
        }

        //STEP 1 (MATCH) : Match the facts and data against the set of rules.
        List<RuleDTO> conflictSet = match(listOfRules, inputData);

        //STEP 2 (RESOLVE) : Resolve the conflict and give the selected one rule.
        RuleDTO resolvedRule = resolve(conflictSet);
        if (null == resolvedRule){
            return null;
        }

        //STEP 3 (EXECUTE) : Run the action of the selected rule on given data and return the output.
        OUTPUT_RESULT outputResult = executeRule(resolvedRule, inputData);

        return outputResult;
    }

    /**
     *We can use here any pattern matching algo:
     * 1. Rete
     * 2. Linear
     * 3. Treat
     * 4. Leaps
     *
     * Here we are using Linear matching algorithm for pattern matching.
     * @param listOfRules
     * @param inputData
     * @return
     */
    protected List<RuleDTO> match(List<RuleDTO> listOfRules, INPUT_DATA inputData){
        return listOfRules.stream()
            .filter(
                rule -> {
                    String condition = rule.getCondition();
                    return ruleParser.parseCondition(condition, inputData);
                }
            )
            .collect(Collectors.toList());
    }

    /**
     * We can use here any resolving techniques:
     * 1. Lex
     * 2. Recency
     * 3. MEA
     * 4. Refactor
     * 5. Priority wise
     *
     *  Here we are using find first rule logic.
     * @param conflictSet
     * @return
     */
    protected RuleDTO resolve(List<RuleDTO> conflictSet){
        Optional<RuleDTO> rule = conflictSet.stream()
            .findFirst();
        if (rule.isPresent()){
            return rule.get();
        }
        return null;
    }

    /**
     * Execute selected rule on input data.
     * @param rule
     * @param inputData
     * @return
     */
    protected OUTPUT_RESULT executeRule(RuleDTO rule, INPUT_DATA inputData){
        OUTPUT_RESULT outputResult = initializeOutputResult();
        return ruleParser.parseAction(rule.getAction(), inputData, outputResult);
    }

    protected abstract OUTPUT_RESULT initializeOutputResult();
    protected abstract RuleNamespace getRuleNamespace();
}
