package com.jun.chu.java.mulithread;

import org.junit.Assert;
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
            Assert.assertNull(ThreadLocal.get());
            Assert.assertEquals("父类数据:inheritableThreadLocal", inheritableThreadLocal.get());
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
            Assert.assertNull(ThreadLocal.get());
            Assert.assertEquals("父类数据:inheritableThreadLocal", inheritableThreadLocal.get());
        }).start();
    }
}
