package com.jun.chu.java;

import lombok.Data;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author chujun
 * @date 2022/11/14
 */
public class SpringELASTTest {
    @Test
    public void test() {
        String template = "#{T(java.lang.Math.random()}";
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(template, new TemplateParserContext());
        String value = expression.getValue(String.class);
        System.out.println(value);
    }

    @Test
    public void test2() {
        String template = "#{#a==#b and 1==1 ? #obj.a.toString() or #obj.b:'hello'}";
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setVariable("a",true);
        evaluationContext.setVariable("b",true);
        Obj obj=new Obj();
        obj.setA(false);
        obj.setB(false);
        evaluationContext.setVariable("obj",obj);
        Expression expression = parser.parseExpression(template, new TemplateParserContext());
        String value = expression.getValue(evaluationContext,String.class);
        System.out.println(value);
    }

    @Test
    public void test03(){
        String template = "#{10+20*30+40+50}";
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setVariable("a",true);
        evaluationContext.setVariable("b",true);
        Obj obj=new Obj();
        obj.setA(false);
        obj.setB(false);
        evaluationContext.setVariable("obj",obj);
        Expression expression = parser.parseExpression(template, new TemplateParserContext());
        Integer value = expression.getValue(evaluationContext,Integer.class);
        System.out.println(value);
    }

    @Data
    public static class Obj{
        private boolean a;
        private boolean b;
    }

}
