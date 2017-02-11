package com.jun.chu.java.proxy.aop.handler;

import java.lang.reflect.InvocationHandler;

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
}
