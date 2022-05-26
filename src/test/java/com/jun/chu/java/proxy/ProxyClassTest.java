package com.jun.chu.java.proxy;

import com.jun.chu.java.proxy.aop.service.Calculator;
import com.jun.chu.java.util.ProxyClassUtils;
import org.junit.Test;

/**
 * @author chujun
 * @date 2022/5/26
 */
public class ProxyClassTest {
    @Test
    public void test() {
        ProxyClassUtils.generateProxyClass("/Users/chujun/my/project/javaddu/target/proxy", "Calculator$proxy0", new Class[]{Calculator.class});
    }
}
