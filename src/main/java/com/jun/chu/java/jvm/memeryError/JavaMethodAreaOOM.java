package com.jun.chu.java.jvm.memeryError;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * jdk8
 * -VM
 * -XX:MetaspaceSize=10M -XX:MaxMetaspaceSize=10M
 *
 * @author chujun
 * @date 2022/1/28
 */
public class JavaMethodAreaOOM {
    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(JavaHeapOOM.OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
                    return proxy.invokeSuper(obj, args);
                }
            });
            enhancer.create();
        }
    }
//    Exception in thread "main" java.lang.OutOfMemoryError: Metaspace
//    at net.sf.cglib.core.AbstractClassGenerator.generate(AbstractClassGenerator.java:348)
//    at net.sf.cglib.proxy.Enhancer.generate(Enhancer.java:492)
//    at net.sf.cglib.core.AbstractClassGenerator$ClassLoaderData.get(AbstractClassGenerator.java:117)
//    at net.sf.cglib.core.AbstractClassGenerator.create(AbstractClassGenerator.java:294)
//    at net.sf.cglib.proxy.Enhancer.createHelper(Enhancer.java:480)
//    at net.sf.cglib.proxy.Enhancer.create(Enhancer.java:305)
//    at com.jun.chu.java.jvm.memeryError.JavaMethodAreaOOM.main(JavaMethodAreaOOM.java:29)
}
