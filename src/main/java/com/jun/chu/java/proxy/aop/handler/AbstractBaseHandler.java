package com.jun.chu.java.proxy.aop.handler;

import java.lang.reflect.InvocationHandler;

/**
 * Created by chujun on 17/2/11.
 */
public abstract class AbstractBaseHandler implements InvocationHandler {
    private Object targetObject;

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }
}
