package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MySequence;
import com.jun.chu.disruptor.MySequenceBarrier;
import com.jun.chu.disruptor.MyWorkerPool;

import java.util.concurrent.Executor;

/**
 * @author chujun
 * @date 2022/2/19
 */
public class MyWorkerPoolInfo<T> implements MyConsumerInfo {
    private final MyWorkerPool<T> workerPool;
    private final MySequenceBarrier sequenceBarrier;
    private boolean endOfChain = true;

    MyWorkerPoolInfo(final MyWorkerPool<T> workerPool, final MySequenceBarrier sequenceBarrier)
    {
        this.workerPool = workerPool;
        this.sequenceBarrier = sequenceBarrier;
    }

    @Override
    public MySequence[] getSequences()
    {
        return workerPool.getWorkerSequences();
    }

    @Override
    public MySequenceBarrier getBarrier()
    {
        return sequenceBarrier;
    }

    @Override
    public boolean isEndOfChain()
    {
        return endOfChain;
    }

    @Override
    public void start(Executor executor)
    {
        workerPool.start(executor);
    }

    @Override
    public void halt()
    {
        workerPool.halt();
    }

    @Override
    public void markAsUsedInBarrier()
    {
        endOfChain = false;
    }

    @Override
    public boolean isRunning()
    {
        return workerPool.isRunning();
    }
}
