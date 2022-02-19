package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MyEventFactory;
import com.jun.chu.disruptor.MyEventHandler;
import com.jun.chu.disruptor.MyExceptionHandler;
import com.jun.chu.disruptor.MyRingBuffer;
import com.jun.chu.disruptor.MySequence;
import com.jun.chu.disruptor.MyWaitStrategy;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author chujun
 * @date 2022/2/16
 */
public class MyDisruptor<T> {
    private final MyRingBuffer<T> ringBuffer;
    private final Executor executor;
    private final MyConsumerRepository<T> consumerRepository = new MyConsumerRepository<>();
    private final AtomicBoolean started = new AtomicBoolean(false);
    private MyExceptionHandler<? super T> exceptionHandler = new MyExceptionHandlerWrapper<>();

    @Deprecated
    public MyDisruptor(final MyEventFactory<T> eventFactory, final int ringBufferSize, final Executor executor)
    {
        this(MyRingBuffer.createMultiProducer(eventFactory, ringBufferSize), executor);
    }

    @Deprecated
    public MyDisruptor(
        final MyEventFactory<T> eventFactory,
        final int ringBufferSize,
        final Executor executor,
        final MyProducerType producerType,
        final MyWaitStrategy waitStrategy)
    {
        this(MyRingBuffer.create(producerType, eventFactory, ringBufferSize, waitStrategy), executor);
    }

    public MyDisruptor(final MyEventFactory<T> eventFactory, final int ringBufferSize, final ThreadFactory threadFactory)
    {
        this(MyRingBuffer.createMultiProducer(eventFactory, ringBufferSize), new MyBasicExecutor(threadFactory));
    }

    public MyDisruptor(
        final MyEventFactory<T> eventFactory,
        final int ringBufferSize,
        final ThreadFactory threadFactory,
        final MyProducerType producerType,
        final MyWaitStrategy waitStrategy)
    {
        this(
            MyRingBuffer.create(producerType, eventFactory, ringBufferSize, waitStrategy),
            new MyBasicExecutor(threadFactory));
    }

    private MyDisruptor(final MyRingBuffer<T> ringBuffer, final Executor executor)
    {
        this.ringBuffer = ringBuffer;
        this.executor = executor;
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    public final MyEventHandlerGroup<T> handleEventsWith(final MyEventHandler<? super T>... handlers)
    {
        return createEventProcessors(new MySequence[0], handlers);
    }
}
