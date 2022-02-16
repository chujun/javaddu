package com.jun.chu.disruptor;

import com.jun.chu.disruptor.util.MyThreadHints;

/**
 * @author chujun
 * @date 2022/2/16
 */
public class MyBusySpinWaitStrategy implements MyWaitStrategy {
    @Override
    public long waitFor(final long sequence, final MySequence cursor, final MySequence dependentSequence, final MySequenceBarrier barrier) throws MyAlertException, InterruptedException, MyTimeoutException {
        long availableSequence;

        while ((availableSequence = dependentSequence.get()) < sequence) {
            barrier.checkAlert();
            MyThreadHints.onSpinWait();
        }

        return availableSequence;
    }

    @Override
    public void signalAllWhenBlocking() {

    }
}
