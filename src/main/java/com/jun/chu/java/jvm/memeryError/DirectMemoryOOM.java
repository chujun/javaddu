package com.jun.chu.java.jvm.memeryError;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * VM:
 * -Xmx20M -XX:MaxDirectMemorySize=10M
 *
 * @author chujun
 * @date 2022/1/28
 */
public class DirectMemoryOOM {
    private final static int _1M = 1024 * 1024;

    public static void main(String[] args) throws IllegalAccessException {
        try {
            Field declaredField = Unsafe.class.getDeclaredFields()[0];
            declaredField.setAccessible(true);
            Unsafe unsafe = (Unsafe) declaredField.get(null);
            while (true) {
                unsafe.allocateMemory(_1M);
            }
        } catch (Throwable e) {
            System.out.println(e);
            throw e;
        }
    }
}
