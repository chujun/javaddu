package com.jun.chu.java.proxy.aop;

import com.jun.chu.java.proxy.aop.handler.AbstractBaseHandler;

import java.lang.reflect.Proxy;

/**
 * Created by chujun on 17/2/11.
 */
public class ProxyFactory {
    public static Object getProxy(Object targetBusinessObject, AbstractBaseHandler handler) {
        Object proxyObject = targetBusinessObject;
        handler.setTargetBusinessObject(targetBusinessObject);

        proxyObject = Proxy.newProxyInstance(targetBusinessObject.getClass().getClassLoader(), targetBusinessObject.getClass().getInterfaces(), handler);

        return proxyObject;
    }
}
