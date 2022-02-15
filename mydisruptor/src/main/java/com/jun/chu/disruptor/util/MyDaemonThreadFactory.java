package com.jun.chu.disruptor.util;

import java.util.concurrent.ThreadFactory;

/**
 * @author chujun
 * @date 2022/2/15
 */
public enum MyDaemonThreadFactory implements ThreadFactory {
    INSTANCE;

    @Override
    public Thread newThread(final Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    }
}
