package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/16
 */
public class MyTimeoutException extends Exception {
    public static final MyTimeoutException INSTANCE = new MyTimeoutException();

    private MyTimeoutException() {
        // Singleton
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
