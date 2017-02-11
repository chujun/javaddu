package com.jun.chu.java.proxy.aop.handler.impl;

import com.jun.chu.java.proxy.aop.handler.AbstractAfterHandler;

import java.lang.reflect.Method;

/**
 * Created by chujun on 17/2/11.
 */
public class AfterHandlerImpl extends AbstractAfterHandler {
    protected void afterHandle(Object proxy, Method method, Object[] args) {
        System.out.println("进行实际后置操作");
    }
}
