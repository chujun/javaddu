package com.jun.chu.disruptor.dsl.stubs;

import com.jun.chu.disruptor.MyWorkHandler;
import com.jun.chu.disruptor.supports.TestEvent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author chujun
 * @date 2022/2/19
 */
public class TestWorkHandler implements MyWorkHandler<TestEvent> {
    private final AtomicBoolean readyToProcessEvent = new AtomicBoolean(false);
    private volatile boolean stopped = false;

    @Override
    public void onEvent(final TestEvent event) throws Exception {
        waitForAndSetFlag(false);
    }

    public void processEvent() {
        waitForAndSetFlag(true);
    }

    public void stopWaiting() {
        stopped = true;
    }

    private void waitForAndSetFlag(final boolean newValue) {
        while (!stopped && !Thread.currentThread().isInterrupted() &&
            !readyToProcessEvent.compareAndSet(!newValue, newValue)) {
            Thread.yield();
        }
    }
}
