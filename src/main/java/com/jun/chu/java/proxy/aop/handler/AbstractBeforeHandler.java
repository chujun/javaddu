package com.jun.chu.java.proxy.aop.handler;

import java.lang.reflect.Method;

/**
 * Created by chujun on 17/2/11.
 */
public abstract class AbstractBeforeHandler extends AbstractBaseHandler {
    protected abstract void handleBefore(Object proxy, Method method, Object[] args);

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("前置操作开始");
        handleBefore(proxy, method, args);
        System.out.println("前置操作结束");
        return super.invoke(proxy, method, args);
    }
}
