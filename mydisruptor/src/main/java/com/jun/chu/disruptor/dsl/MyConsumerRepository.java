package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MyEventHandler;
import com.jun.chu.disruptor.MyEventProcessor;
import com.jun.chu.disruptor.MySequence;
import com.jun.chu.disruptor.MySequenceBarrier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
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

    @Override
    public Iterator<MyConsumerInfo> iterator() {
        return consumerInfos.iterator();
    }
}
