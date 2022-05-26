package com.jun.chu.java.proxy;

import com.jun.chu.java.proxy.cglib.AliSmsService;
import com.jun.chu.java.proxy.cglib.CglibProxyFactory;
import com.jun.chu.java.proxy.cglib.DebugMethodInterceptor;
import org.junit.Test;

/**
 * @author chujun
 * @date 2022/5/26
 */
public class CglibProxyTest {
    @Test
    public void test() {
        AliSmsService aliSmsService = CglibProxyFactory.getProxy(AliSmsService.class,
            new DebugMethodInterceptor());
        aliSmsService.send("java");
        //before method send
        //send message:java
        //after method send
    }

    @Test
    public void testSendFinal() {
        AliSmsService aliSmsService = CglibProxyFactory.getProxy(AliSmsService.class,
            new DebugMethodInterceptor());
        aliSmsService.sendFinal("java");
        //sendV2 message:java
    }

}
