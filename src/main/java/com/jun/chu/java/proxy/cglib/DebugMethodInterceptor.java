package com.jun.chu.java.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author chujun
 * @date 2022/5/26
 */
public class DebugMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
        //调用方法之前，我们可以添加自己的操作
        System.out.println("before method: " + method.getName());
        //调用被代理对象的原始方法
        Object object = proxy.invokeSuper(obj, args);
        //调用方法之后，我们同样可以添加自己的操作
        System.out.println("after method: " + method.getName());
        return object;
    }
}
