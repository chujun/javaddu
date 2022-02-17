package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MySequence;
import com.jun.chu.disruptor.MySequenceBarrier;

import java.util.concurrent.Executor;

/**
 * @author chujun
 * @date 2022/2/17
 */
public interface MyConsumerInfo {
    MySequence[] getSequences();

    MySequenceBarrier getBarrier();

    boolean isEndOfChain();

    void start(Executor executor);

    void halt();

    void markAsUsedInBarrier();

    boolean isRunning();
}
