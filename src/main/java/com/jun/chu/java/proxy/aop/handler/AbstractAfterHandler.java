package com.jun.chu.java.proxy.aop.handler;

import java.lang.reflect.Method;

/**
 * Created by chujun on 17/2/11.
 */
public abstract class AbstractAfterHandler extends AbstractBaseHandler {
    protected abstract void afterHandle(Object proxy, Method method, Object[] args);

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = super.invoke(proxy, method, args);
        System.out.println("后置操作开始");
        afterHandle(proxy, method, args);
        System.out.println("后置操作结束");
        return result;
    }


}
