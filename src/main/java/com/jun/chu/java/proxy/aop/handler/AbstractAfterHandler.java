package com.jun.chu.java.proxy.aop.handler;

import java.lang.reflect.Method;

/**
 * Created by chujun on 17/2/11.
 */
public abstract class AbstractAfterHandler extends AbstractBaseHandler {
    protected abstract void afterHandle(Object proxy, Method method, Object[] args);

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(getTargetBusinessObject(), args);
        afterHandle(proxy, method, args);
        return result;
    }


}
