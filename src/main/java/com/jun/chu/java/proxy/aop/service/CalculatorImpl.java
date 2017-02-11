package com.jun.chu.java.proxy.aop.service;

public class CalculatorImpl implements Calculator {
    public int calculate(int a, int b) {
        System.out.println("真实业务操作");
        return a / b;
    }
}


