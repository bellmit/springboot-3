package com.leo.boot.aop.annotation.handler;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

public class SpelContext {

    private static final ExpressionParser expressionParser = new SpelExpressionParser();

    private EvaluationContext context;

    public static SpelContext of(Method method, String[] argNames, Object[] argValues) {
        return new SpelContext(method, argNames, argValues);
    }
    
    public String getValue(String expression) {
        return getValue(expression, String.class);
    }

    public <T> T getValue(String expression, @Nullable Class<T> desiredResultType) {
        return expressionParser.parseExpression(expression).getValue(this.context, desiredResultType);
    }

    private SpelContext(Method method, String[] argNames, Object[] argValues) {
        this.context = new StandardEvaluationContext(method);
        for (int i = 0; i < argNames.length; i++) {
            this.context.setVariable(argNames[i], argValues[i]);
        }
    }
    
}
