package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MyBatchEventProcessor;
import com.jun.chu.disruptor.MyEventFactory;
import com.jun.chu.disruptor.MyEventHandler;
import com.jun.chu.disruptor.MyEventProcessor;
import com.jun.chu.disruptor.MyEventTranslator;
import com.jun.chu.disruptor.MyEventTranslatorOneArg;
import com.jun.chu.disruptor.MyEventTranslatorThreeArg;
import com.jun.chu.disruptor.MyEventTranslatorTwoArg;
import com.jun.chu.disruptor.MyExceptionHandler;
import com.jun.chu.disruptor.MyRingBuffer;
import com.jun.chu.disruptor.MySequence;
import com.jun.chu.disruptor.MySequenceBarrier;
import com.jun.chu.disruptor.MyTimeoutException;
import com.jun.chu.disruptor.MyWaitStrategy;
import com.jun.chu.disruptor.MyWorkHandler;
import com.jun.chu.disruptor.MyWorkerPool;
import com.jun.chu.disruptor.util.MyUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
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
        //设置空Sequence数组作为SequenceBarrier的sequences
        return createEventProcessors(new MySequence[0], handlers);
    }

    @SafeVarargs
    public final MyEventHandlerGroup<T> handleEventsWith(final MyEventProcessorFactory<T>... eventProcessorFactories) {
        final MySequence[] barrierSequences = new MySequence[0];
        return createEventProcessors(barrierSequences, eventProcessorFactories);
    }

    public MyEventHandlerGroup<T> handleEventsWith(final MyEventProcessor... processors) {
        for (final MyEventProcessor processor : processors) {
            //消费者仓库添加消费者
            consumerRepository.add(processor);
        }

        final MySequence[] sequences = new MySequence[processors.length];
        for (int i = 0; i < processors.length; i++) {
            sequences[i] = processors[i].getSequence();
        }
        //ringBuffer gatingSequences添加处理链终端sequences
        ringBuffer.addGatingSequences(sequences);
        //返回eventHandlerGroup组
        return new MyEventHandlerGroup<>(this, consumerRepository, MyUtil.getSequencesFor(processors));
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public final MyEventHandlerGroup<T> handleEventsWithWorkerPool(final MyWorkHandler<T>... workHandlers) {
        return createWorkerPool(new MySequence[0], workHandlers);
    }

    public void handleExceptionsWith(final MyExceptionHandler<? super T> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @SuppressWarnings("unchecked")
    public void setDefaultExceptionHandler(final MyExceptionHandler<? super T> exceptionHandler) {
        checkNotStarted();
        if (!(this.exceptionHandler instanceof MyExceptionHandlerWrapper)) {
            throw new IllegalStateException("setDefaultExceptionHandler can not be used after handleExceptionsWith");
        }
        ((MyExceptionHandlerWrapper<T>) this.exceptionHandler).switchTo(exceptionHandler);
    }

    public MyExceptionHandlerSetting<T> handleExceptionsFor(final MyEventHandler<T> eventHandler) {
        return new MyExceptionHandlerSetting<>(eventHandler, consumerRepository);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public final MyEventHandlerGroup<T> after(final MyEventHandler<T>... handlers) {
        final MySequence[] sequences = new MySequence[handlers.length];
        for (int i = 0, handlersLength = handlers.length; i < handlersLength; i++) {
            sequences[i] = consumerRepository.getSequenceFor(handlers[i]);
        }

        return new MyEventHandlerGroup<>(this, consumerRepository, sequences);
    }

    public MyEventHandlerGroup<T> after(final MyEventProcessor... processors) {
        for (final MyEventProcessor processor : processors) {
            consumerRepository.add(processor);
        }

        return new MyEventHandlerGroup<>(this, consumerRepository, MyUtil.getSequencesFor(processors));
    }

    public void publishEvent(final MyEventTranslator<T> eventTranslator) {
        ringBuffer.publishEvent(eventTranslator);
    }

    public <A> void publishEvent(final MyEventTranslatorOneArg<T, A> eventTranslator, final A arg) {
        ringBuffer.publishEvent(eventTranslator, arg);
    }

    public <A> void publishEvents(final MyEventTranslatorOneArg<T, A> eventTranslator, final A[] arg) {
        ringBuffer.publishEvents(eventTranslator, arg);
    }

    public <A, B> void publishEvent(final MyEventTranslatorTwoArg<T, A, B> eventTranslator, final A arg0, final B arg1) {
        ringBuffer.publishEvent(eventTranslator, arg0, arg1);
    }

    public <A, B, C> void publishEvent(final MyEventTranslatorThreeArg<T, A, B, C> eventTranslator, final A arg0, final B arg1, final C arg2) {
        ringBuffer.publishEvent(eventTranslator, arg0, arg1, arg2);
    }

    /**
     * 该方法必须在所有事件处理器已经添加后才能调用
     */
    public MyRingBuffer<T> start() {
        checkOnlyStartedOnce();
        for (final MyConsumerInfo consumerInfo : consumerRepository) {
            consumerInfo.start(executor);
        }

        return ringBuffer;
    }

    public void halt()
    {
        for (final MyConsumerInfo consumerInfo : consumerRepository)
        {
            consumerInfo.halt();
        }
    }

    public void shutdown()
    {
        try
        {
            shutdown(-1, TimeUnit.MILLISECONDS);
        }
        catch (final MyTimeoutException e)
        {
            exceptionHandler.handleOnShutdownException(e);
        }
    }

    public void shutdown(final long timeout, final TimeUnit timeUnit) throws MyTimeoutException
    {
        final long timeOutAt = System.currentTimeMillis() + timeUnit.toMillis(timeout);
        while (hasBacklog())
        {
            if (timeout >= 0 && System.currentTimeMillis() > timeOutAt)
            {
                throw MyTimeoutException.INSTANCE;
            }
            // Busy spin
        }
        halt();
    }
    public MyRingBuffer<T> getRingBuffer()
    {
        return ringBuffer;
    }

    public long getCursor()
    {
        return ringBuffer.getCursor();
    }

    public long getBufferSize()
    {
        return ringBuffer.getBufferSize();
    }

    public T get(final long sequence)
    {
        return ringBuffer.get(sequence);
    }

    /**
     * 获取指定EventHandler的SequenceBarrier，该SequenceBarrier可能被多个eventHandler共享
     */
    public MySequenceBarrier getBarrierFor(final MyEventHandler<T> handler)
    {
        return consumerRepository.getBarrierFor(handler);
    }

    /**
     * 查询指定eventhandler当前处理的sequence值
     */
    public long getSequenceValueFor(final MyEventHandler<T> b1)
    {
        return consumerRepository.getSequenceFor(b1).get();
    }

    /**
     * 判断所有事件是否已经被所有消费者消费
     */
    private boolean hasBacklog()
    {
        final long cursor = ringBuffer.getCursor();
        for (final MySequence consumer : consumerRepository.getLastSequenceInChain(false))
        {
            if (cursor > consumer.get())
            {
                return true;
            }
        }
        return false;
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
    private void updateGatingSequencesForNextInChain(final MySequence[] barrierSequences, final MySequence[] processorSequences) {
        if (processorSequences.length > 0) {
            //添加处理链的终点Sequences到ringBuffer的gatingSequences
            ringBuffer.addGatingSequences(processorSequences);
            for (final MySequence barrierSequence : barrierSequences) {
                //移除ringBuffer的gatingSequences中非处理链终点的Sequences
                ringBuffer.removeGatingSequence(barrierSequence);
            }
            //标记barrierSequences为处理链的非终点
            consumerRepository.unMarkEventProcessorsAsEndOfChain(barrierSequences);
        }
    }

    MyEventHandlerGroup<T> createEventProcessors(
        final MySequence[] barrierSequences, final MyEventProcessorFactory<T>[] processorFactories) {
        final MyEventProcessor[] eventProcessors = new MyEventProcessor[processorFactories.length];
        for (int i = 0; i < processorFactories.length; i++) {
            eventProcessors[i] = processorFactories[i].createEventProcessor(ringBuffer, barrierSequences);
        }

        return handleEventsWith(eventProcessors);
    }

    MyEventHandlerGroup<T> createWorkerPool(
        final MySequence[] barrierSequences, final MyWorkHandler<? super T>[] workHandlers) {
        final MySequenceBarrier sequenceBarrier = ringBuffer.newBarrier(barrierSequences);
        final MyWorkerPool<T> workerPool = new MyWorkerPool<>(ringBuffer, sequenceBarrier, exceptionHandler, workHandlers);

        //消费者仓库添加消费者：WorkerPoolInfo类型
        consumerRepository.add(workerPool, sequenceBarrier);

        final MySequence[] workerSequences = workerPool.getWorkerSequences();

        updateGatingSequencesForNextInChain(barrierSequences, workerSequences);

        return new MyEventHandlerGroup<>(this, consumerRepository, workerSequences);
    }

    private void checkNotStarted() {
        if (started.get()) {
            throw new IllegalStateException("All event handlers must be added before calling starts.");
        }
    }

    private void checkOnlyStartedOnce() {
        if (!started.compareAndSet(false, true)) {
            throw new IllegalStateException("Disruptor.start() must only be called once.");
        }
    }

    @Override
    public String toString() {
        return "MyDisruptor{" +
            "ringBuffer=" + ringBuffer +
            ", started=" + started +
            ", executor=" + executor +
            '}';
    }
}
