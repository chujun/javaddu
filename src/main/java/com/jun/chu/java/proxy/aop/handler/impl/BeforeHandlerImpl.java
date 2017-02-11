package com.jun.chu.java.proxy.aop.handler.impl;

import com.jun.chu.java.proxy.aop.handler.AbstractBeforeHandler;

import java.lang.reflect.Method;

/**
 * Created by chujun on 17/2/11.
 */
public class BeforeHandlerImpl extends AbstractBeforeHandler {
    protected void handleBefore(Object proxy, Method method, Object[] args) {
        System.out.println("进行前置操作");
    }
}
