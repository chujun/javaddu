package com.jun.chu.disruptor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chujun
 * @date 2022/2/18
 */
public final class MyBatchEventProcessor<T> implements MyEventProcessor {
    private static final int IDLE = 0;
    private static final int HALTED = IDLE + 1;
    private static final int RUNNING = HALTED + 1;

    private final AtomicInteger running = new AtomicInteger(IDLE);
    private MyExceptionHandler<? super T> exceptionHandler;
    private final MyDataProvider<T> dataProvider;
    private final MySequenceBarrier sequenceBarrier;
    private final MyEventHandler<? super T> eventHandler;
    //已消费的Sequence
    private final MySequence sequence = new MySequence(MySequencer.INITIAL_CURSOR_VALUE);
    private final MyTimeoutHandler timeoutHandler;
    private final MyBatchStartAware batchStartAware;

    public MyBatchEventProcessor(
        final MyDataProvider<T> dataProvider,
        final MySequenceBarrier sequenceBarrier,
        final MyEventHandler<? super T> eventHandler) {
        this.dataProvider = dataProvider;
        this.sequenceBarrier = sequenceBarrier;
        this.eventHandler = eventHandler;

        if (eventHandler instanceof MySequenceReportingEventHandler) {
            ((MySequenceReportingEventHandler<?>) eventHandler).setSequenceCallback(sequence);
        }

        batchStartAware =
            (eventHandler instanceof MyBatchStartAware) ? (MyBatchStartAware) eventHandler : null;
        timeoutHandler =
            (eventHandler instanceof MyTimeoutHandler) ? (MyTimeoutHandler) eventHandler : null;
    }

    @Override
    public MySequence getSequence() {
        return sequence;
    }

    @Override
    public void halt() {
        running.set(HALTED);
        sequenceBarrier.alert();
    }

    @Override
    public boolean isRunning() {
        return running.get() != IDLE;
    }

    public void setExceptionHandler(final MyExceptionHandler<? super T> exceptionHandler) {
        if (null == exceptionHandler) {
            throw new NullPointerException();
        }

        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void run() {
        if (running.compareAndSet(IDLE, RUNNING)) {
            sequenceBarrier.clearAlert();

            notifyStart();
            try {
                if (running.get() == RUNNING) {
                    processEvents();
                }
            } finally {
                notifyShutdown();
                running.set(IDLE);
            }
        } else {
            //jdk9之后就有compareAndExchange方法了
            // This is a little bit of guess work.  The running state could of changed to HALTED by
            // this point.  However, Java does not have compareAndExchange which is the only way
            // to get it exactly correct.
            if (running.get() == RUNNING) {
                throw new IllegalStateException("Thread is already running");
            } else {
                earlyExit();
            }
        }
    }

    private void processEvents() {
        T event = null;
        long nextSequence = sequence.get() + 1L;

        while (true) {
            try {
                //可消费的sequence值,可能比nextSequence大很多，生产者生产消息很多的话
                final long availableSequence = sequenceBarrier.waitFor(nextSequence);
                if (batchStartAware != null) {
                    batchStartAware.onBatchStart(availableSequence - nextSequence + 1);
                }

                while (nextSequence <= availableSequence) {
                    event = dataProvider.get(nextSequence);
                    eventHandler.onEvent(event, nextSequence, nextSequence == availableSequence);
                    nextSequence++;
                }

                sequence.set(availableSequence);
            } catch (MyTimeoutException e) {
                notifyTimeout(sequence.get());
            } catch (MyAlertException ex) {
                if (running.get() != RUNNING) {
                    break;
                }
            } catch (final Throwable ex) {
                handleEventException(ex, nextSequence, event);
                sequence.set(nextSequence);
                nextSequence++;
            }
        }
    }

    private void earlyExit() {
        notifyStart();
        notifyShutdown();
    }

    private void notifyTimeout(final long availableSequence) {
        try {
            if (timeoutHandler != null) {
                timeoutHandler.onTimeout(availableSequence);
            }
        } catch (Throwable e) {
            handleEventException(e, availableSequence, null);
        }
    }

    private void notifyStart() {
        if (eventHandler instanceof MyLifecycleAware) {
            try {
                ((MyLifecycleAware) eventHandler).onStart();
            } catch (final Throwable ex) {
                handleOnStartException(ex);
            }
        }
    }

    private void notifyShutdown() {
        if (eventHandler instanceof MyLifecycleAware) {
            try {
                ((MyLifecycleAware) eventHandler).onShutdown();
            } catch (final Throwable ex) {
                handleOnShutdownException(ex);
            }
        }
    }

    private void handleEventException(final Throwable ex, final long sequence, final T event) {
        getExceptionHandler().handleEventException(ex, sequence, event);
    }

    private void handleOnStartException(final Throwable ex) {
        getExceptionHandler().handleOnStartException(ex);
    }


    private void handleOnShutdownException(final Throwable ex) {
        getExceptionHandler().handleOnShutdownException(ex);
    }

    private MyExceptionHandler<? super T> getExceptionHandler() {
        MyExceptionHandler<? super T> handler = exceptionHandler;
        if (handler == null) {
            return MyExceptionHandlers.defaultHandler();
        }
        return handler;
    }
}
