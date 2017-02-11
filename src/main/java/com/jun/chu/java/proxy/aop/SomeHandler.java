package com.jun.chu.java.proxy.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by chujun on 17/2/11.
 * 常规套路
 */
class SomeHandler implements InvocationHandler {
    private Object targetObject;

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        // Your complex business validation and logic
        Object result = method.invoke(targetObject ,params);
        return result;
    }

}
