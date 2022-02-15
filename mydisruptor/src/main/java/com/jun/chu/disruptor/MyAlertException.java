package com.jun.chu.disruptor;

/**
 * TODO:cj 作用
 * 用于警告EventProcessor等待{@link MySequenceBarrier}状态改变
 * 因为性能原因这个异常并没有添加进堆栈(stack trace)
 *
 * @author chujun
 * @date 2022/2/15
 */
public class MyAlertException extends Exception {
    /**
     * 预分配异常避免gc
     */
    public static final MyAlertException INSTANCE = new MyAlertException();

    private MyAlertException() {
    }

    /**
     * 因为性能原因这个异常并没有添加进堆栈(stack trace) 通过覆盖该方法实现
     * @return
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
