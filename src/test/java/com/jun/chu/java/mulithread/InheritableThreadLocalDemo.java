package com.jun.chu.java.mulithread;

import org.junit.Test;

/**
 * @author chujun
 * @date 2022/6/6
 */
public class InheritableThreadLocalDemo {

    @Test
    public void test() {
        ThreadLocal<String> ThreadLocal = new ThreadLocal<>();
        ThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
        ThreadLocal.set("父类数据:threadLocal");
        inheritableThreadLocal.set("父类数据:inheritableThreadLocal");

        new Thread(() -> {
            System.out.println("子线程获取父类ThreadLocal数据：" + ThreadLocal.get());
            System.out.println("子线程获取父类inheritableThreadLocal数据：" + inheritableThreadLocal.get());
        }).start();
    }

    @Test
    public void test2() {
        ThreadLocal<String> ThreadLocal = new ThreadLocal<>();
        ThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
        ThreadLocal.set("父类数据:threadLocal");
        inheritableThreadLocal.set("父类数据V0:inheritableThreadLocal");
        inheritableThreadLocal.set("父类数据:inheritableThreadLocal");

        new Thread(() -> {
            System.out.println("子线程获取父类ThreadLocal数据：" + ThreadLocal.get());
            System.out.println("子线程获取父类inheritableThreadLocal数据：" + inheritableThreadLocal.get());
        }).start();
    }
}
