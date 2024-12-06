package io.yody.yodemy.rule.langParser;

import org.mvel2.ParserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.mvel2.MVEL;

import java.util.Arrays;
import java.util.Map;

@Service
public class MVELParser {
    private final Logger log = LoggerFactory.getLogger(MVELParser.class);

    public boolean parseMvelExpression(String expression, Map<String, Object> inputObjects) {
        try {
            ParserContext context = new ParserContext();

            context.addImport("Arrays", Arrays.class);

            Object compiledExpression = MVEL.compileExpression(expression, context);

            return (boolean) MVEL.executeExpression(compiledExpression, inputObjects);

        } catch (Exception e) {
            log.error("Can not parse Mvel Expression: {} Error: {}", expression, e.getMessage());
            return false;
        }
    }
}

