package com.jun.chu.disruptor;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 方便实现基于jdk logging，Level.SEVERE并且重新包装成RuntimeException异常抛出
 *
 * @author chujun
 * @date 2022/2/16
 */
public class MyFatalExceptionHandler implements MyExceptionHandler<Object> {
    private static final Logger LOGGER = Logger.getLogger(MyFatalExceptionHandler.class.getName());
    private final Logger logger;

    public MyFatalExceptionHandler() {
        this.logger = LOGGER;
    }

    public MyFatalExceptionHandler(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handleEventException(final Throwable ex, final long sequence, final Object event) {
        logger.log(Level.SEVERE, "Exception processing: " + sequence + " " + event, ex);

        throw new RuntimeException(ex);
    }

    @Override
    public void handleOnStartException(final Throwable ex) {
        logger.log(Level.SEVERE, "Exception during onStart()", ex);
    }

    @Override
    public void handleOnShutdownException(final Throwable ex) {
        logger.log(Level.SEVERE, "Exception during onShutdown()", ex);
    }
}
