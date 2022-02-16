package com.jun.chu.disruptor;

import com.jun.chu.disruptor.util.MyThreadHints;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chujun
 * @date 2022/2/16
 */
public class MyLiteBlockingWaitStrategy implements MyWaitStrategy {
    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = lock.newCondition();
    private final AtomicBoolean signalNeeded = new AtomicBoolean(false);

    @Override
    public long waitFor(final long sequence, final MySequence cursor, final MySequence dependentSequence, final MySequenceBarrier barrier) throws MyAlertException, InterruptedException, MyTimeoutException {
        long availableSequence;
        if (cursor.get() < sequence) {
            lock.lock();

            try {
                do {
                    signalNeeded.getAndSet(true);

                    if (cursor.get() >= sequence) {
                        break;
                    }

                    barrier.checkAlert();
                    processorNotifyCondition.await();
                }
                while (cursor.get() < sequence);
            } finally {
                lock.unlock();
            }
        }

        while ((availableSequence = dependentSequence.get()) < sequence) {
            barrier.checkAlert();
            MyThreadHints.onSpinWait();
        }

        return availableSequence;
    }

    @Override
    public void signalAllWhenBlocking() {
        if (signalNeeded.getAndSet(false)) {
            lock.lock();
            try {
                processorNotifyCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }
}
