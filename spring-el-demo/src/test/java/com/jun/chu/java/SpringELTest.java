package com.jun.chu.java;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author chujun
 * @date 2022/11/10
 */
public class SpringELTest {
    @Test
    public void test() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'");
        String message = (String) exp.getValue();
        Assert.assertEquals("Hello World", message);


        try {
            exp = parser.parseExpression("H W");
            message = (String) exp.getValue();
            Assert.assertEquals("H W", message);
            Assert.fail();
        } catch (SpelParseException exception) {
            Assert.assertEquals("EL1041E: After parsing a valid expression, there is still more data in the expression: 'W'", exception.getMessage());
        }
    }

    @Test
    public void testNestProperties() {
        //测试SpringEL解析器
        String template = "#{#systemA.field2?.toString}";//设置文字模板,其中#{}表示表达式的起止，
        ExpressionParser parser = new SpelExpressionParser();//创建表达式解析器

        //通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        SystemA systemA = new SystemA();
        context.setVariable("systemA", systemA);

        //解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
        Expression expression = parser.parseExpression(template, new TemplateParserContext());

        //使用Expression.getValue()获取表达式的值，这里传入了Evalution上下文，第二个参数是类型参数，表示返回值的类型。
        Assert.assertNull(expression.getValue(context, String.class));

        //测试field2字段为null的场景
        template = "#{#systemA.field2.toString}";
        try {
            expression = parser.parseExpression(template, new TemplateParserContext());
            Assert.assertNull(expression.getValue(context, String.class));
            Assert.fail();
        } catch (SpelEvaluationException e) {
            Assert.assertEquals("EL1007E: Property or field 'toString' cannot be found on null", e.getMessage());
        }
    }

    @Test
    public void testRootObject() {
        // Create and set a calendar
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);

        // The constructor arguments are name, birthday, and nationality.
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");

        ExpressionParser parser = new SpelExpressionParser();

        Expression exp = parser.parseExpression("name"); // Parse name as an expression
        Assert.assertEquals("Nikola Tesla", exp.getValue(tesla, String.class));


        exp = parser.parseExpression("name == 'Nikola Tesla'");
        Assert.assertTrue(exp.getValue(tesla, Boolean.class));
    }

    @Test
    public void testEvaluationContext() {
        //测试SpringEL解析器
        String template = "#{#user+#userB}，早上好";//设置文字模板,其中#{}表示表达式的起止，#user是表达式字符串，表示引用一个变量。
        ExpressionParser paser = new SpelExpressionParser();//创建表达式解析器

        //通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("user", "黎明");
        context.setVariable("userB", "初生");

        //解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
        Expression expression = paser.parseExpression(template, new TemplateParserContext());

        //使用Expression.getValue()获取表达式的值，这里传入了Evalution上下文，第二个参数是类型参数，表示返回值的类型。
        System.out.println(expression.getValue(context, String.class));
    }

    @Test
    public void testCompare() {
        //测试SpringEL解析器
        String template = "#{#systemA.field1 == #systemB.field2}";//设置文字模板,其中#{}表示表达式的起止，#user是表达式字符串，表示引用一个变量。
        ExpressionParser parser = new SpelExpressionParser();//创建表达式解析器

        //通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        SystemA systemA = new SystemA();
        systemA.setField1("1");
        systemA.setField2("field2");
        SystemB systemB = new SystemB();
        systemB.setField2("2");
        context.setVariable("systemA", systemA);
        context.setVariable("systemB", systemB);

        //解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
        Expression expression = parser.parseExpression(template, new TemplateParserContext());

        //使用Expression.getValue()获取表达式的值，这里传入了Evalution上下文，第二个参数是类型参数，表示返回值的类型。
        Assert.assertFalse(expression.getValue(context, Boolean.class));

        systemB.setField2("1");
        expression = parser.parseExpression(template, new TemplateParserContext());
        Assert.assertTrue(expression.getValue(context, Boolean.class));

        systemB.setField2("222");
        Assert.assertFalse(expression.getValue(context, Boolean.class));
    }

    @Test
    public void testCollectionProjection() {
        //4.3.17. Collection Projection集合映射
        String template = "#{#systemA.systemBs.![amount]}";//设置文字模板,其中#{}表示表达式的起止，#user是表达式字符串，表示引用一个变量。
        ExpressionParser parser = new SpelExpressionParser();//创建表达式解析器

        //通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        SystemA systemA = new SystemA();
        systemA.setField1("1");
        systemA.setField2("field2");
        systemA.setAmount(100);

        SystemB systemB1 = new SystemB();
        systemB1.setAmount(30);
        SystemB systemB2 = new SystemB();
        systemB2.setAmount(50);
        SystemB systemB3 = new SystemB();
        systemB3.setAmount(20);
        systemA.setSystemBs(Lists.newArrayList(systemB1, systemB2, systemB3));

        context.setVariable("systemA", systemA);


        //解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
        Expression expression = parser.parseExpression(template, new TemplateParserContext());

        //使用Expression.getValue()获取表达式的值，这里传入了Evalution上下文，第二个参数是类型参数，表示返回值的类型。
        Assert.assertEquals("[30, 50, 20]", expression.getValue(context, List.class).toString());
    }

    @Test
    public void testStaticMethod() {
        //4.3.17. Collection Projection集合映射
        String template = "#{T(com.jun.chu.java.ListOperation).sum(#systemA.systemBs.![amount])}";//设置文字模板,其中#{}表示表达式的起止，#user是表达式字符串，表示引用一个变量。
        ExpressionParser parser = new SpelExpressionParser();//创建表达式解析器

        //通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        SystemA systemA = new SystemA();
        systemA.setField1("1");
        systemA.setField2("field2");
        systemA.setAmount(100);

        SystemB systemB1 = new SystemB();
        systemB1.setAmount(30);
        SystemB systemB2 = new SystemB();
        systemB2.setAmount(50);
        SystemB systemB3 = new SystemB();
        systemB3.setAmount(20);
        systemA.setSystemBs(Lists.newArrayList(systemB1, systemB2, systemB3));

        context.setVariable("systemA", systemA);


        //解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
        Expression expression = parser.parseExpression(template, new TemplateParserContext());

        //使用Expression.getValue()获取表达式的值，这里传入了Evalution上下文，第二个参数是类型参数，表示返回值的类型。
        Assert.assertEquals(100, expression.getValue(context, Integer.class).intValue());
    }

    @Test
    public void testListSum() {
        //4.3.17. Collection Projection集合映射
        String template = "#{#systemA.amount == T(com.jun.chu.java.ListOperation).sum(#systemA.systemBs.![amount])}";//设置文字模板,其中#{}表示表达式的起止，#user是表达式字符串，表示引用一个变量。
        ExpressionParser parser = new SpelExpressionParser();//创建表达式解析器

        //通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        SystemA systemA = new SystemA();
        systemA.setField1("1");
        systemA.setField2("field2");
        systemA.setAmount(100);

        SystemB systemB1 = new SystemB();
        systemB1.setAmount(30);
        SystemB systemB2 = new SystemB();
        systemB2.setAmount(50);
        SystemB systemB3 = new SystemB();
        systemB3.setAmount(20);
        systemA.setSystemBs(Lists.newArrayList(systemB1, systemB2, systemB3));

        context.setVariable("systemA", systemA);


        //解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
        Expression expression = parser.parseExpression(template, new TemplateParserContext());

        //使用Expression.getValue()获取表达式的值，这里传入了Evalution上下文，第二个参数是类型参数，表示返回值的类型。
        Assert.assertTrue(expression.getValue(context, Boolean.class));

    }

    @Test
    public void testListSumWith() {
        //Collection Projection集合映射?.与累加
        String template = "#{#systemA.amount == T(com.jun.chu.java.ListOperation).sum(#systemA.systemBs?.![amount])}";//设置文字模板,其中#{}表示表达式的起止，#user是表达式字符串，表示引用一个变量。
        ExpressionParser parser = new SpelExpressionParser();//创建表达式解析器

        //通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        SystemA systemA = new SystemA();
        systemA.setField1("1");
        systemA.setField2("field2");
        systemA.setAmount(100);

        SystemB systemB1 = new SystemB();
        systemB1.setAmount(30);
        SystemB systemB2 = new SystemB();
        systemB2.setAmount(50);
        SystemB systemB3 = new SystemB();
        systemB3.setAmount(20);
        //systemA.setSystemBs(Lists.newArrayList(systemB1, systemB2, systemB3));

        context.setVariable("systemA", systemA);


        //解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
        Expression expression = parser.parseExpression(template, new TemplateParserContext());

        //使用Expression.getValue()获取表达式的值，这里传入了Evalution上下文，第二个参数是类型参数，表示返回值的类型。
        Assert.assertFalse(expression.getValue(context, Boolean.class));

    }

    @Test
    public void testOperator() {
        //Collection Projection集合映射?.与累加
        String template = "#{#systemA?.amount == 100 * #systemB?.amount }";//设置文字模板,其中#{}表示表达式的起止，#user是表达式字符串，表示引用一个变量。
        ExpressionParser parser = new SpelExpressionParser();//创建表达式解析器

        //通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        SystemA systemA = new SystemA();
        systemA.setField1("1");
        systemA.setField2("field2");
        systemA.setAmount(300);

        SystemB systemB = new SystemB();
        systemB.setAmount(3);

        context.setVariable("systemA", systemA);
        context.setVariable("systemB", systemB);


        //解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
        Expression expression = parser.parseExpression(template, new TemplateParserContext());

        //使用Expression.getValue()获取表达式的值，这里传入了Evalution上下文，第二个参数是类型参数，表示返回值的类型。
        Assert.assertTrue(expression.getValue(context, Boolean.class));

        //测试值为null场景
        systemB.setAmount(null);
        try {
            Assert.assertFalse(expression.getValue(context, Boolean.class));
            Assert.fail();
        } catch (SpelEvaluationException e) {
            Assert.assertEquals("EL1030E: The operator 'MULTIPLY' is not supported between objects of type 'java.lang.Integer' and 'null'", e.getMessage());
        }
    }


    private static ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    public static Object getValueByExpression(Object targetObject, String propertyName) {
        try {
            EvaluationContext evaluationContext = new StandardEvaluationContext();
            evaluationContext.setVariable("model", targetObject);
            Expression expression = EXPRESSION_PARSER.parseExpression(String.format("#{#model.%s}", propertyName), new TemplateParserContext());
            return expression.getValue(evaluationContext, Object.class);
        } catch (ParseException e) {
            //log.warn("ParseException analysis the parameter is {}",propertyName);
            System.out.println("ParseException analysis the parameter is " + propertyName);
            throw new RuntimeException(e);
        } catch (EvaluationException e) {
            //log.warn("EvaluationException analysis the parameter is {}",propertyName);
            System.out.println("EvaluationException analysis the parameter is " + propertyName);
            throw new RuntimeException(e);
        }
    }

    public static class SystemA {
        private String field1;
        private String field2;
        private Integer amount;
        private List<SystemB> systemBs;

        public String getField1() {
            return field1;
        }

        public void setField1(final String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(final String field2) {
            this.field2 = field2;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(final Integer amount) {
            this.amount = amount;
        }

        public List<SystemB> getSystemBs() {
            return systemBs;
        }

        public void setSystemBs(final List<SystemB> systemBs) {
            this.systemBs = systemBs;
        }
    }

    public static class SystemB {
        private String field2;
        private Integer amount;

        public String getField2() {
            return field2;
        }

        public void setField2(final String field2) {
            this.field2 = field2;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(final Integer amount) {
            this.amount = amount;
        }
    }


}
