package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/15
 */
public class MyInsufficientCapacityException extends Exception {
    public static final MyInsufficientCapacityException INSTANCE = new MyInsufficientCapacityException();

    private MyInsufficientCapacityException() {
        // Singleton
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
