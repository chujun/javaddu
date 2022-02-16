package com.jun.chu.disruptor;

/**
 * 异常回调处理接口，在MyBatchEventProcessor的事件循环处理过程中
 *
 * @author chujun
 * @date 2022/2/16
 */
public interface MyExceptionHandler<T> {
    /**
     * 事件处理过程中抛出的异常处理策略
     * 如果策略是希望终止MyBatchEventProcessor后续的事件处理，则应该抛出RuntimeException
     *
     * @param ex
     * @param sequence
     * @param event
     */
    void handleEventException(Throwable ex, long sequence, T event);

    void handleOnStartException(Throwable ex);

    void handleOnShutdownException(Throwable ex);
}
