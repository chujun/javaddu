package com.jun.chu.disruptor;

import com.jun.chu.disruptor.util.MyUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author chujun
 * @date 2022/2/17
 */
public final class MyWorkerPool<T> {
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final MySequence workSequence = new MySequence(MySequencer.INITIAL_CURSOR_VALUE);
    private final MyRingBuffer<T> ringBuffer;
    // WorkProcessors are created to wrap each of the provided WorkHandlers
    private final MyWorkProcessor<?>[] workProcessors;

    public MyWorkerPool(
        final MyRingBuffer<T> ringBuffer,
        final MySequenceBarrier sequenceBarrier,
        final MyExceptionHandler<? super T> exceptionHandler,
        final MyWorkHandler<? super T>... workHandlers) {
        this.ringBuffer = ringBuffer;
        final int numWorkers = workHandlers.length;
        workProcessors = new MyWorkProcessor[numWorkers];

        for (int i = 0; i < numWorkers; i++) {
            workProcessors[i] = new MyWorkProcessor<>(
                ringBuffer,
                sequenceBarrier,
                workHandlers[i],
                exceptionHandler,
                workSequence);
        }
    }

    public MyWorkerPool(
        final MyEventFactory<T> eventFactory,
        final MyExceptionHandler<? super T> exceptionHandler,
        final MyWorkHandler<? super T>... workHandlers) {
        ringBuffer = MyRingBuffer.createMultiProducer(eventFactory, 1024, new MyBlockingWaitStrategy());
        final MySequenceBarrier barrier = ringBuffer.newBarrier();
        final int numWorkers = workHandlers.length;
        workProcessors = new MyWorkProcessor[numWorkers];

        for (int i = 0; i < numWorkers; i++) {
            workProcessors[i] = new MyWorkProcessor<>(
                ringBuffer,
                barrier,
                workHandlers[i],
                exceptionHandler,
                workSequence);
        }

        ringBuffer.addGatingSequences(getWorkerSequences());
    }

    public MySequence[] getWorkerSequences() {
        final MySequence[] sequences = new MySequence[workProcessors.length + 1];
        for (int i = 0, size = workProcessors.length; i < size; i++) {
            sequences[i] = workProcessors[i].getSequence();
        }
        sequences[sequences.length - 1] = workSequence;

        return sequences;
    }

    public MyRingBuffer<T> start(final Executor executor) {
        if (!started.compareAndSet(false, true)) {
            throw new IllegalStateException("WorkerPool has already been started and cannot be restarted until halted.");
        }

        final long cursor = ringBuffer.getCursor();
        workSequence.set(cursor);

        for (MyWorkProcessor<?> processor : workProcessors) {
            processor.getSequence().set(cursor);
            executor.execute(processor);
        }

        return ringBuffer;
    }

    public void drainAndHalt() {
        MySequence[] workerSequences = getWorkerSequences();
        while (ringBuffer.getCursor() > MyUtil.getMinimumSequence(workerSequences)) {
            Thread.yield();
        }

        for (MyWorkProcessor<?> processor : workProcessors) {
            processor.halt();
        }

        started.set(false);
    }

    public void halt() {
        for (MyWorkProcessor<?> processor : workProcessors) {
            processor.halt();
        }

        started.set(false);
    }

    public boolean isRunning() {
        return started.get();
    }

}
