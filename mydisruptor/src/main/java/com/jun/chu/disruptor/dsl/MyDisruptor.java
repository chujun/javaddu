package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MyBatchEventProcessor;
import com.jun.chu.disruptor.MyEventFactory;
import com.jun.chu.disruptor.MyEventHandler;
import com.jun.chu.disruptor.MyExceptionHandler;
import com.jun.chu.disruptor.MyRingBuffer;
import com.jun.chu.disruptor.MySequence;
import com.jun.chu.disruptor.MySequenceBarrier;
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
    public MyDisruptor(final MyEventFactory<T> eventFactory, final int ringBufferSize, final Executor executor) {
        this(MyRingBuffer.createMultiProducer(eventFactory, ringBufferSize), executor);
    }

    @Deprecated
    public MyDisruptor(
        final MyEventFactory<T> eventFactory,
        final int ringBufferSize,
        final Executor executor,
        final MyProducerType producerType,
        final MyWaitStrategy waitStrategy) {
        this(MyRingBuffer.create(producerType, eventFactory, ringBufferSize, waitStrategy), executor);
    }

    public MyDisruptor(final MyEventFactory<T> eventFactory, final int ringBufferSize, final ThreadFactory threadFactory) {
        this(MyRingBuffer.createMultiProducer(eventFactory, ringBufferSize), new MyBasicExecutor(threadFactory));
    }

    public MyDisruptor(
        final MyEventFactory<T> eventFactory,
        final int ringBufferSize,
        final ThreadFactory threadFactory,
        final MyProducerType producerType,
        final MyWaitStrategy waitStrategy) {
        this(
            MyRingBuffer.create(producerType, eventFactory, ringBufferSize, waitStrategy),
            new MyBasicExecutor(threadFactory));
    }

    private MyDisruptor(final MyRingBuffer<T> ringBuffer, final Executor executor) {
        this.ringBuffer = ringBuffer;
        this.executor = executor;
    }

    /**
     * 设置ringbuffer事件处理的EventHandler  事件处理链的开头
     * <p>This method can be used as the start of a chain. For example if the handler <code>A</code> must
     * process events before handler <code>B</code>:</p>
     * <pre><code>dw.handleEventsWith(A).then(B);</code></pre>
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public final MyEventHandlerGroup<T> handleEventsWith(final MyEventHandler<? super T>... handlers) {
        return createEventProcessors(new MySequence[0], handlers);
    }


    /**
     * 批量创建一批事件处理组EventHandlerGroup
     */
    MyEventHandlerGroup<T> createEventProcessors(
        final MySequence[] barrierSequences,
        final MyEventHandler<? super T>[] eventHandlers) {
//检查disruptor没有启动起来
        checkNotStarted();

        final MySequence[] processorSequences = new MySequence[eventHandlers.length];
        //创建Sequence屏障
        final MySequenceBarrier barrier = ringBuffer.newBarrier(barrierSequences);

        for (int i = 0, eventHandlersLength = eventHandlers.length; i < eventHandlersLength; i++) {
            final MyEventHandler<? super T> eventHandler = eventHandlers[i];

            final MyBatchEventProcessor<T> batchEventProcessor =
                new MyBatchEventProcessor<>(ringBuffer, barrier, eventHandler);

            if (exceptionHandler != null) {
                batchEventProcessor.setExceptionHandler(exceptionHandler);
            }
            //消费者仓库添加消费者
            consumerRepository.add(batchEventProcessor, eventHandler, barrier);
            processorSequences[i] = batchEventProcessor.getSequence();
        }
        //更新ringBuffer的gatingSequence
        updateGatingSequencesForNextInChain(barrierSequences, processorSequences);
        //组装eventHandlerGroup事件处理组
        return new MyEventHandlerGroup<>(this, consumerRepository, processorSequences);
    }

    /**
     * 更新ringBuffer的gatingSequence并更新barrierSequences的处理链终点关系
     */
    private void updateGatingSequencesForNextInChain(final MySequence[] barrierSequences, final MySequence[] processorSequences)
    {
        if (processorSequences.length > 0)
        {
            //添加处理链的终点Sequences到ringBuffer的gatingSequences
            ringBuffer.addGatingSequences(processorSequences);
            for (final MySequence barrierSequence : barrierSequences)
            {
                //移除ringBuffer的gatingSequences中非处理链终点的Sequences
                ringBuffer.removeGatingSequence(barrierSequence);
            }
            //标记barrierSequences为处理链的非终点
            consumerRepository.unMarkEventProcessorsAsEndOfChain(barrierSequences);
        }
    }

    private void checkNotStarted() {
        if (started.get()) {
            throw new IllegalStateException("All event handlers must be added before calling starts.");
        }
    }
}
