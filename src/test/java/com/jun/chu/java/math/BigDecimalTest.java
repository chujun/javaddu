package com.jun.chu.java.math;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author chujun
 * @date 2022/5/29
 */
public class BigDecimalTest {
    @Test
    public void test() {
        //阿里巴巴手册这两个是精确的
        BigDecimal a = new BigDecimal("0.1");
        BigDecimal b = BigDecimal.valueOf(0.1);
        double d = 0.1;
        System.out.println(d);
        Assert.assertEquals(0, a.compareTo(b));
        //这个是不精准的
        System.out.println(new BigDecimal(0.1));
    }

    @Test
    public void test2() {
        double d = new BigDecimal("1").divide(new BigDecimal("10")).doubleValue();
        System.out.println(d);
    }
}
