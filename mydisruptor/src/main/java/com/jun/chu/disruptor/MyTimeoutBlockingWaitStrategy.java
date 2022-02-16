package com.jun.chu.disruptor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chujun
 * @date 2022/2/16
 */
public class MyTimeoutBlockingWaitStrategy implements MyWaitStrategy {
    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = lock.newCondition();
    private final long timeoutInNanos;

    public MyTimeoutBlockingWaitStrategy(final long timeout, final TimeUnit units) {
        timeoutInNanos = units.toNanos(timeout);
    }

    @Override
    public long waitFor(final long sequence, final MySequence cursor, final MySequence dependentSequence, final MySequenceBarrier barrier) throws MyAlertException, InterruptedException, MyTimeoutException {
        long nanos = timeoutInNanos;

        long availableSequence;
        if (cursor.get() < sequence) {
            lock.lock();
            try {
                while (cursor.get() < sequence) {
                    barrier.checkAlert();
                    nanos = processorNotifyCondition.awaitNanos(nanos);
                    if (nanos <= 0) {
                        throw MyTimeoutException.INSTANCE;
                    }
                }
            } finally {
                lock.unlock();
            }
        }

        while ((availableSequence = dependentSequence.get()) < sequence) {
            barrier.checkAlert();
        }

        return availableSequence;
    }

    @Override
    public void signalAllWhenBlocking() {
        lock.lock();
        try {
            processorNotifyCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "MyTimeoutBlockingWaitStrategy{" +
            "processorNotifyCondition=" + processorNotifyCondition +
            '}';
    }
}
