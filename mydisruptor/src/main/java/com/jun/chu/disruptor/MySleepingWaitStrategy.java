package com.jun.chu.disruptor;

import java.util.concurrent.locks.LockSupport;

/**
 * 等待策略：
 * 等待办法一开始使用自旋,然后降级成Thread.yield,最终变成(<code>LockSupport.parkNanos(n)</code>)
 * 这个策略在性能和cpu资源之间有很好的妥协
 *
 * @author chujun
 * @date 2022/2/16
 */
public class MySleepingWaitStrategy implements MyWaitStrategy {
    private static final int DEFAULT_RETRIES = 200;
    private static final long DEFAULT_SLEEP = 100;

    private final int retries;
    private final long sleepTimeNs;

    public MySleepingWaitStrategy() {
        this(DEFAULT_RETRIES, DEFAULT_SLEEP);
    }

    public MySleepingWaitStrategy(int retries) {
        this(retries, DEFAULT_SLEEP);
    }

    public MySleepingWaitStrategy(int retries, long sleepTimeNs) {
        this.retries = retries;
        this.sleepTimeNs = sleepTimeNs;
    }

    @Override
    public long waitFor(final long sequence, final MySequence cursor, final MySequence dependentSequence, final MySequenceBarrier barrier) throws MyAlertException, InterruptedException, MyTimeoutException {
        long availableSequence;
        int counter = retries;

        while ((availableSequence = dependentSequence.get()) < sequence) {
            counter = applyWaitMethod(barrier, counter);
        }

        return availableSequence;
    }

    @Override
    public void signalAllWhenBlocking() {

    }

    private int applyWaitMethod(final MySequenceBarrier barrier, int counter)
        throws MyAlertException {
        barrier.checkAlert();

        if (counter > 100) {
            //先自旋转
            --counter;
        } else if (counter > 0) {
            //再Thread.yield();
            --counter;
            Thread.yield();
        } else {
            //最终LockSupport.parkNanos(sleepTimeNs);
            LockSupport.parkNanos(sleepTimeNs);
        }

        return counter;
    }
}
