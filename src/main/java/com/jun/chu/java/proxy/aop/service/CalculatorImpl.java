package com.jun.chu.java.proxy.aop.service;

public class CalculatorImpl implements Calculator {
    public int calculate(int a, int b) {
        System.out.println("真实业务操作开始");
        System.out.println(a + "/" + b);
        int result = a / b;
        System.out.println("真实业务操作结束");
        return result;
    }
}


