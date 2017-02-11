package com.jun.chu.java.proxy.aop.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by chujun on 17/2/11.
 */
public abstract class AbstractBaseHandler implements InvocationHandler {
    //实际业务接口对象
    private Object targetBusinessObject;

    public Object getTargetBusinessObject() {
        return targetBusinessObject;
    }

    public void setTargetBusinessObject(Object targetBusinessObject) {
        this.targetBusinessObject = targetBusinessObject;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("------------注意这里调用的是业务接口的method,而非proxy,proxy:" + proxy.getClass().getName());
        return method.invoke(getTargetBusinessObject(), args);
    }
}
