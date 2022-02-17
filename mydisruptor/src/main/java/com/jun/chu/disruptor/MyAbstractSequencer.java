package com.jun.chu.disruptor;

import com.jun.chu.disruptor.util.MyUtil;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author chujun
 * @date 2022/2/17
 */
public abstract class MyAbstractSequencer implements MySequencer{
    private static final AtomicReferenceFieldUpdater<MyAbstractSequencer, MySequence[]> SEQUENCE_UPDATER =
        AtomicReferenceFieldUpdater.newUpdater(MyAbstractSequencer.class, MySequence[].class, "gatingSequences");

    protected final int bufferSize;
    protected final MyWaitStrategy waitStrategy;
    protected final MySequence cursor = new MySequence(MySequencer.INITIAL_CURSOR_VALUE);
    //TODO:cj 作用 维护消费者消费的sequence列表
    protected volatile MySequence[] gatingSequences = new MySequence[0];

    public MyAbstractSequencer(int bufferSize, MyWaitStrategy waitStrategy)
    {
        if (bufferSize < 1)
        {
            throw new IllegalArgumentException("bufferSize must not be less than 1");
        }
        if (Integer.bitCount(bufferSize) != 1)
        {
            throw new IllegalArgumentException("bufferSize must be a power of 2");
        }

        this.bufferSize = bufferSize;
        this.waitStrategy = waitStrategy;
    }

    @Override
    public final long getCursor()
    {
        return cursor.get();
    }

    @Override
    public final int getBufferSize()
    {
        return bufferSize;
    }

    @Override
    public final void addGatingSequences(MySequence... gatingSequences)
    {
        MySequenceGroups.addSequences(this, SEQUENCE_UPDATER, this, gatingSequences);
    }

    @Override
    public boolean removeGatingSequence(MySequence sequence)
    {
        return MySequenceGroups.removeSequence(this, SEQUENCE_UPDATER, sequence);
    }

    @Override
    public long getMinimumSequence()
    {
        return MyUtil.getMinimumSequence(gatingSequences, cursor.get());
    }

    @Override
    public MySequenceBarrier newBarrier(MySequence... sequencesToTrack)
    {
        return new MyProcessingSequenceBarrier(this, waitStrategy, cursor, sequencesToTrack);
    }

//    @Override
//    public <T> EventPoller<T> newPoller(DataProvider<T> dataProvider, Sequence... gatingSequences)
//    {
//        return EventPoller.newInstance(dataProvider, this, new Sequence(), cursor, gatingSequences);
//    }

    @Override
    public String toString()
    {
        return "MyAbstractSequencer{" +
            "waitStrategy=" + waitStrategy +
            ", cursor=" + cursor +
            ", gatingSequences=" + Arrays.toString(gatingSequences) +
            '}';
    }
}
