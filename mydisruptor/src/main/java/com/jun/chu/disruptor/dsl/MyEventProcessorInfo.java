package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MyEventHandler;
import com.jun.chu.disruptor.MyEventProcessor;
import com.jun.chu.disruptor.MySequence;
import com.jun.chu.disruptor.MySequenceBarrier;

import java.util.concurrent.Executor;

/**
 * 消费者,
 * 将特定事件处理舞台整合在一起的包装器
 * 持有EventProcessor引用，EventHandler引用和SequenceBarrier引用
 *
 * @author chujun
 * @date 2022/2/17
 */
public class MyEventProcessorInfo<T> implements MyConsumerInfo {
    private final MyEventProcessor eventprocessor;
    private final MyEventHandler<? super T> handler;
    private final MySequenceBarrier barrier;
    private boolean endOfChain = true;

    MyEventProcessorInfo(
        final MyEventProcessor eventprocessor, final MyEventHandler<? super T> handler, final MySequenceBarrier barrier) {
        this.eventprocessor = eventprocessor;
        this.handler = handler;
        this.barrier = barrier;
    }

    public MyEventProcessor getEventProcessor() {
        return eventprocessor;
    }

    @Override
    public MySequence[] getSequences() {
        return new MySequence[]{eventprocessor.getSequence()};
    }

    public MyEventHandler<? super T> getHandler() {
        return handler;
    }

    @Override
    public MySequenceBarrier getBarrier() {
        return barrier;
    }

    @Override
    public boolean isEndOfChain() {
        return endOfChain;
    }

    @Override
    public void start(final Executor executor) {
        executor.execute(eventprocessor);
    }

    @Override
    public void halt() {
        eventprocessor.halt();
    }

    @Override
    public void markAsUsedInBarrier() {
        endOfChain = false;
    }

    @Override
    public boolean isRunning() {
        return eventprocessor.isRunning();
    }
}
