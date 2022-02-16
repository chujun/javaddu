package com.jun.chu.disruptor;

/**
 * Strategy employed for making {@link MyEventProcessor}s wait on a cursor {@link MySequence}.
 *
 * @author chujun
 * @date 2022/2/16
 */
public interface MyWaitStrategy {
    /**
     * 等待指定sequcnce可提供.这是有可能的：返回一个小于指定sequence的数值，这取决于接口实现
     * 并发访问
     * @param sequence
     * @param cursor
     * @param dependentSequence
     * @param barrier
     * @return
     * @throws MyAlertException       if the status of the Disruptor has changed.
     * @throws InterruptedException if the thread is interrupted.
     * @throws MyTimeoutException if a timeout occurs before waiting completes (not used by some strategies)
     */
    long waitFor(long sequence, MySequence cursor, MySequence dependentSequence, MySequenceBarrier barrier)
        throws MyAlertException, InterruptedException, MyTimeoutException;

    void signalAllWhenBlocking();
}
