package com.jun.chu.java.transaction.isolation;

/**
 * @author chujun
 * @date 2022/3/5
 */
public class Assert {
    public static <T extends RuntimeException> void isTrue(boolean expression, T e) {
        if (!expression) {
            throw e;
        }
    }
}
