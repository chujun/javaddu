package com.jun.chu.disruptor;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author chujun
 * @date 2022/2/17
 */
public final class MyWorkProcessor<T> implements MyEventProcessor {
    private final AtomicBoolean running = new AtomicBoolean(false);
    //WorkProcessor自己的Sequence
    private final MySequence sequence = new MySequence(MySequencer.INITIAL_CURSOR_VALUE);
    private final MyRingBuffer<T> ringBuffer;
    private final MySequenceBarrier sequenceBarrier;
    private final MyWorkHandler<? super T> workHandler;
    private final MyExceptionHandler<? super T> exceptionHandler;
    //workerPool的sequence
    private final MySequence workSequence;

    private final MyEventReleaser eventReleaser = new MyEventReleaser() {
        @Override
        public void release() {
            sequence.set(Long.MAX_VALUE);
        }
    };

    private final MyTimeoutHandler timeoutHandler;

    public MyWorkProcessor(
        final MyRingBuffer<T> ringBuffer,
        final MySequenceBarrier sequenceBarrier,
        final MyWorkHandler<? super T> workHandler,
        final MyExceptionHandler<? super T> exceptionHandler,
        final MySequence workSequence) {
        this.ringBuffer = ringBuffer;
        this.sequenceBarrier = sequenceBarrier;
        this.workHandler = workHandler;
        this.exceptionHandler = exceptionHandler;
        this.workSequence = workSequence;

        if (this.workHandler instanceof MyEventReleaseAware) {
            ((MyEventReleaseAware) this.workHandler).setEventReleaser(eventReleaser);
        }

        timeoutHandler = (workHandler instanceof MyTimeoutHandler) ? (MyTimeoutHandler) workHandler : null;
    }

    @Override
    public MySequence getSequence() {
        return sequence;
    }

    @Override
    public void halt() {
        running.set(false);
        sequenceBarrier.alert();
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    /**
     * TODO:cj 比较核心方法
     * It is ok to have another thread re-run this method after a halt().
     */
    @Override
    public void run() {
        if (!running.compareAndSet(false, true)) {
            throw new IllegalStateException("Thread is already running");
        }
        sequenceBarrier.clearAlert();
        notifyStart();
        //标记已处理sequence
        boolean processedSequence = true;
        long cachedAvailableSequence = Long.MIN_VALUE;
        long nextSequence = sequence.get();
        T event = null;
        while (true) {
            try {
                if (processedSequence) {
                    //已处理sequence后，都要CAS无锁拿到下一个需要处理的workpool的sequence
                    processedSequence = false;
                    //CAS无锁更新workpool的sequence
                    do {

                        nextSequence = workSequence.get() + 1L;
                        //获取workpool处理的消费的最新游标，并更新到work自身上去
                        sequence.set(nextSequence - 1L);
                    }
                    while (!workSequence.compareAndSet(nextSequence - 1L, nextSequence));
                }

                if (cachedAvailableSequence >= nextSequence) {
                    //存在可消费事件
                    event = ringBuffer.get(nextSequence);
                    workHandler.onEvent(event);
                    processedSequence = true;
                } else {
                    //不存在可消费事件
                    cachedAvailableSequence = sequenceBarrier.waitFor(nextSequence);
                }
            } catch (final MyTimeoutException e) {
                notifyTimeout(sequence.get());
            } catch (final MyAlertException ex) {
                if (!running.get()) {
                    break;
                }
            } catch (final Throwable ex) {
                // handle, mark as processed, unless the exception handler threw an exception
                exceptionHandler.handleEventException(ex, nextSequence, event);
                processedSequence = true;
            }
        }
        notifyShutdown();

        running.set(false);
    }

    private void notifyTimeout(final long availableSequence) {
        try {
            if (timeoutHandler != null) {
                timeoutHandler.onTimeout(availableSequence);
            }
        } catch (Throwable e) {
            exceptionHandler.handleEventException(e, availableSequence, null);
        }
    }

    private void notifyStart() {
        if (workHandler instanceof MyLifecycleAware) {
            try {
                ((MyLifecycleAware) workHandler).onStart();
            } catch (final Throwable ex) {
                exceptionHandler.handleOnStartException(ex);
            }
        }
    }

    private void notifyShutdown() {
        if (workHandler instanceof MyLifecycleAware) {
            try {
                ((MyLifecycleAware) workHandler).onShutdown();
            } catch (final Throwable ex) {
                exceptionHandler.handleOnShutdownException(ex);
            }
        }
    }

}
