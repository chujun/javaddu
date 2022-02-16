package com.jun.chu.disruptor;

import java.util.concurrent.TimeUnit;

/**
 * Spins, then yields, then waits using the configured fallback WaitStrategy.
 *
 * @author chujun
 * @date 2022/2/16
 */
public class MyPhasedBackoffWaitStrategy implements MyWaitStrategy {
    private static final int SPIN_TRIES = 10000;
    private final long spinTimeoutNanos;
    private final long yieldTimeoutNanos;
    private final MyWaitStrategy fallbackStrategy;

    public MyPhasedBackoffWaitStrategy(
        long spinTimeout,
        long yieldTimeout,
        TimeUnit units,
        MyWaitStrategy fallbackStrategy) {
        this.spinTimeoutNanos = units.toNanos(spinTimeout);
        this.yieldTimeoutNanos = spinTimeoutNanos + units.toNanos(yieldTimeout);
        this.fallbackStrategy = fallbackStrategy;
    }


    public static MyPhasedBackoffWaitStrategy withLock(
        long spinTimeout,
        long yieldTimeout,
        TimeUnit units) {
        return new MyPhasedBackoffWaitStrategy(
            spinTimeout, yieldTimeout,
            units, new MyBlockingWaitStrategy());
    }

    public static MyPhasedBackoffWaitStrategy withLiteLock(
        long spinTimeout,
        long yieldTimeout,
        TimeUnit units) {
        return new MyPhasedBackoffWaitStrategy(
            spinTimeout, yieldTimeout,
            units, new MyLiteBlockingWaitStrategy());
    }

    public static MyPhasedBackoffWaitStrategy withSleep(
        long spinTimeout,
        long yieldTimeout,
        TimeUnit units) {
        return new MyPhasedBackoffWaitStrategy(
            spinTimeout, yieldTimeout,
            units, new MySleepingWaitStrategy(0));
    }

    @Override
    public long waitFor(final long sequence, final MySequence cursor, final MySequence dependentSequence, final MySequenceBarrier barrier) throws MyAlertException, InterruptedException, MyTimeoutException {
        long availableSequence;
        long startTime = 0;
        int counter = SPIN_TRIES;

        do {
            if ((availableSequence = dependentSequence.get()) >= sequence) {
                return availableSequence;
            }

            if (0 == --counter) {
                if (0 == startTime) {
                    startTime = System.nanoTime();
                } else {
                    long timeDelta = System.nanoTime() - startTime;
                    if (timeDelta > yieldTimeoutNanos) {
                        return fallbackStrategy.waitFor(sequence, cursor, dependentSequence, barrier);
                    } else if (timeDelta > spinTimeoutNanos) {
                        Thread.yield();
                    }
                }
                counter = SPIN_TRIES;
            }
        }
        while (true);
    }

    @Override
    public void signalAllWhenBlocking() {
        fallbackStrategy.signalAllWhenBlocking();
    }
}
