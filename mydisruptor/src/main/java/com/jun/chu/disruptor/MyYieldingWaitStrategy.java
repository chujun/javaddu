package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/16
 */
public class MyYieldingWaitStrategy implements MyWaitStrategy {
    private static final int SPIN_TRIES = 100;

    @Override
    public long waitFor(final long sequence, final MySequence cursor, final MySequence dependentSequence, final MySequenceBarrier barrier) throws MyAlertException, InterruptedException, MyTimeoutException {
        long availableSequence;
        int counter = SPIN_TRIES;

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

        if (0 == counter) {
            Thread.yield();
        } else {
            --counter;
        }

        return counter;
    }
}
