package com.jun.chu.disruptor.dsl.stubs;

import com.jun.chu.disruptor.util.MyDaemonThreadFactory;
import org.junit.Assert;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chujun
 * @date 2022/2/19
 */
public class StubThreadFactory implements ThreadFactory {
    private final MyDaemonThreadFactory threadFactory = MyDaemonThreadFactory.INSTANCE;
    private final Collection<Thread> threads = new CopyOnWriteArrayList<Thread>();
    private final AtomicBoolean ignoreExecutions = new AtomicBoolean(false);
    private final AtomicInteger executionCount = new AtomicInteger(0);

    @Override
    public Thread newThread(final Runnable command) {
        executionCount.getAndIncrement();
        Runnable toExecute = command;
        if (ignoreExecutions.get()) {
            toExecute = new NoOpRunnable();
        }
        final Thread thread = threadFactory.newThread(toExecute);
        thread.setName(command.toString());
        threads.add(thread);
        return thread;
    }

    public void joinAllThreads() {
        for (Thread thread : threads) {
            if (thread.isAlive()) {
                try {
                    thread.interrupt();
                    thread.join(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Assert.assertFalse("Failed to stop thread: " + thread, thread.isAlive());
        }

        threads.clear();
    }

    public void ignoreExecutions() {
        ignoreExecutions.set(true);
    }

    public int getExecutionCount() {
        return executionCount.get();
    }

    private static final class NoOpRunnable implements Runnable {
        @Override
        public void run() {
        }
    }
}
