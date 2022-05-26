package com.jun.chu.java.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author chujun
 * @date 2022/5/26
 */
public class CglibProxyFactory {
    public static <T> T getProxy(Class<T> clazz, MethodInterceptor methodInterceptor) {
        // 创建动态代理增强类
        Enhancer enhancer = new Enhancer();
        // 设置类加载器
        enhancer.setClassLoader(clazz.getClassLoader());
        // 设置被代理类
        enhancer.setSuperclass(clazz);
        // 设置方法拦截器
        enhancer.setCallback(methodInterceptor);
        // 创建代理类
        return (T) enhancer.create();
    }
}
