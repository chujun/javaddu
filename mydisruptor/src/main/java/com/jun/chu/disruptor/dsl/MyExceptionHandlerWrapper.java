package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MyExceptionHandler;
import com.jun.chu.disruptor.MyExceptionHandlers;

/**
 * MyExceptionHandler包装器
 *
 * @author chujun
 * @date 2022/2/16
 */
public class MyExceptionHandlerWrapper<T> implements MyExceptionHandler<T> {
    /**
     * 真实MyExceptionHandler
     */
    private MyExceptionHandler<? super T> delegate;

    public void switchTo(final MyExceptionHandler<? super T> exceptionHandler) {
        this.delegate = exceptionHandler;
    }

    @Override
    public void handleEventException(final Throwable ex, final long sequence, final T event) {
        getExceptionHandler().handleEventException(ex, sequence, event);
    }

    @Override
    public void handleOnStartException(final Throwable ex) {
        getExceptionHandler().handleOnStartException(ex);
    }

    @Override
    public void handleOnShutdownException(final Throwable ex) {
        getExceptionHandler().handleOnShutdownException(ex);
    }

    private MyExceptionHandler<? super T> getExceptionHandler() {
        MyExceptionHandler<? super T> handler = delegate;
        if (handler == null) {
            //返货默认异常处理器
            return MyExceptionHandlers.defaultHandler();
        }
        return handler;
    }
}
