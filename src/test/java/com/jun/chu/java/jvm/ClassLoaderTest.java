package com.jun.chu.java.jvm;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author chujun
 * @date 2022/5/28
 */
public class ClassLoaderTest {
    /**
     * 不同加载器相同类名,两个类不"相等"
     */
    @Test
    public void test() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader myClassLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(final String name) throws ClassNotFoundException {
                String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                InputStream is = getClass().getResourceAsStream(fileName);
                if (null == is) {
                    //java.lang.Object该类加载器加载不到,找父加载器加载
                    return super.loadClass(name);
                }
                try {
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(null, b, 0, b.length, null);
                } catch (IOException e) {
                    throw new ClassNotFoundException(name);
                }

            }
        };
        Object obj = myClassLoader.loadClass("com.jun.chu.java.jvm.ClassLoaderTest").newInstance();
        System.out.println(obj.getClass());
        Assert.assertFalse(obj instanceof ClassLoaderTest);
    }

}
