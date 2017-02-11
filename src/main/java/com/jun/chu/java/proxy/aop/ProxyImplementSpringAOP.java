package com.jun.chu.java.proxy.aop;

import com.jun.chu.java.proxy.aop.service.Calculator;
import com.jun.chu.java.proxy.aop.service.CalculatorImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by chujun on 17/2/11.
 * 参考资料:
 * http://mp.weixin.qq.com/s?__biz=MjM5NzMyMjAwMA==&mid=2651478197&idx=1&sn=875a8d5458416a87f1c5dd64ee5cd332&chksm=bd2534ca8a52bddc3ff84f3b7f296598b28e99e1b4b53baeda9c4e78e0ccacb3540f399f3a6d&mpshare=1&scene=1&srcid=0210hqGsL4nlBOXKo2mUtk0p#rd
 * <p>
 * 印象笔记 https://app.yinxiang.com/shard/s23/nl/1787756087/462d0549-b073-40d6-9c00-d52a4bb54279/
 *
 * 参考源码
 * https://github.com/debjava/aopusingjdkdynamicproxy/tree/master/src/main/java/com/ddlab/rnd
 *
 * <p>
 * 本demo作用
 * 利用core java实现aop的简单功能
 * <p>
 * Spring是借助了JDK proxy和CGlib两种技术实现AOP的。
 * JDK dynamic proxy提供了一种灵活的方式来hook一个方法并执行指定的操作，
 * 但执行操作时得有一个限制条件：必须先提供一个相关的接口以及该接口的实现类。
 */
public class ProxyImplementSpringAOP {
    public static void main(String[] args){
        CalculatorImpl bussiness = new CalculatorImpl();
    }
}


