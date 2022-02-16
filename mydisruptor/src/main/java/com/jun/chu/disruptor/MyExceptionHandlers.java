package com.jun.chu.disruptor;

/**
 * 静态访问默认MyExceptionHandler对象
 *
 * @author chujun
 * @date 2022/2/16
 */
public class MyExceptionHandlers {
    private MyExceptionHandlers() {
    }

    public static MyExceptionHandler<Object> defaultHandler() {
        return MyDefaultExceptionHandlerHolder.HANDLER;
    }

    private static final class MyDefaultExceptionHandlerHolder {
        private static final MyExceptionHandler<Object> HANDLER = new MyFatalExceptionHandler();
    }
}
