package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MyEventHandler;
import com.jun.chu.disruptor.MyEventProcessor;
import com.jun.chu.disruptor.MySequence;
import com.jun.chu.disruptor.MySequenceBarrier;
import com.jun.chu.disruptor.MyWorkerPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 仓库：维护所有消费者关系
 *
 * @author chujun
 * @date 2022/2/17
 */
public class MyConsumerRepository<T> implements Iterable<MyConsumerInfo> {
    private final Map<MyEventHandler<?>, MyEventProcessorInfo<T>> eventProcessorInfoByEventHandler =
        new IdentityHashMap<>();
    private final Map<MySequence, MyConsumerInfo> eventProcessorInfoBySequence =
        new IdentityHashMap<>();
    private final Collection<MyConsumerInfo> consumerInfos = new ArrayList<>();

    public void add(
        final MyEventProcessor eventprocessor,
        final MyEventHandler<? super T> handler,
        final MySequenceBarrier barrier) {
        //三要素构成消费者
        final MyEventProcessorInfo<T> consumerInfo = new MyEventProcessorInfo<>(eventprocessor, handler, barrier);
        eventProcessorInfoByEventHandler.put(handler, consumerInfo);
        eventProcessorInfoBySequence.put(eventprocessor.getSequence(), consumerInfo);
        consumerInfos.add(consumerInfo);
    }

    /**
     * 通过该方法添加的processor，eventProcessorInfoByEventHandler map没有eventhandler映射
     * MyEventProcessorInfo也没有handler和barrier
     */
    public void add(final MyEventProcessor processor) {
        final MyEventProcessorInfo<T> consumerInfo = new MyEventProcessorInfo<>(processor, null, null);
        eventProcessorInfoBySequence.put(processor.getSequence(), consumerInfo);
        consumerInfos.add(consumerInfo);
    }

    public void add(final MyWorkerPool<T> workerPool, final MySequenceBarrier sequenceBarrier) {
        final MyWorkerPoolInfo<T> workerPoolInfo = new MyWorkerPoolInfo<>(workerPool, sequenceBarrier);
        consumerInfos.add(workerPoolInfo);
        for (MySequence sequence : workerPool.getWorkerSequences()) {
            eventProcessorInfoBySequence.put(sequence, workerPoolInfo);
        }
    }

    /**
     * 返回所有处于处理链终端的消费者的Sequence数组
     */
    public MySequence[] getLastSequenceInChain(boolean includeStopped) {
        List<MySequence> lastSequence = new ArrayList<>();
        for (MyConsumerInfo consumerInfo : consumerInfos) {
            if ((includeStopped || consumerInfo.isRunning()) && consumerInfo.isEndOfChain()) {
                final MySequence[] sequences = consumerInfo.getSequences();
                Collections.addAll(lastSequence, sequences);
            }
        }

        return lastSequence.toArray(new MySequence[lastSequence.size()]);
    }

    public MyEventProcessor getEventProcessorFor(final MyEventHandler<T> handler) {
        final MyEventProcessorInfo<T> eventprocessorInfo = getEventProcessorInfo(handler);
        if (eventprocessorInfo == null) {
            throw new IllegalArgumentException("The event handler " + handler + " is not processing events.");
        }

        return eventprocessorInfo.getEventProcessor();
    }

    public MySequence getSequenceFor(final MyEventHandler<T> handler) {
        return getEventProcessorFor(handler).getSequence();
    }

    public void unMarkEventProcessorsAsEndOfChain(final MySequence... barrierEventProcessors) {
        for (MySequence barrierEventProcessor : barrierEventProcessors) {
            getEventProcessorInfo(barrierEventProcessor).markAsUsedInBarrier();
        }
    }

    @Override
    public Iterator<MyConsumerInfo> iterator() {
        return consumerInfos.iterator();
    }

    public MySequenceBarrier getBarrierFor(final MyEventHandler<T> handler) {
        final MyConsumerInfo consumerInfo = getEventProcessorInfo(handler);
        return consumerInfo != null ? consumerInfo.getBarrier() : null;
    }

    private MyEventProcessorInfo<T> getEventProcessorInfo(final MyEventHandler<T> handler) {
        return eventProcessorInfoByEventHandler.get(handler);
    }

    private MyConsumerInfo getEventProcessorInfo(final MySequence barrierEventProcessor) {
        return eventProcessorInfoBySequence.get(barrierEventProcessor);
    }
}
