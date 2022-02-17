package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/17
 */
public final class MyProcessingSequenceBarrier implements MySequenceBarrier{
    private final MyWaitStrategy waitStrategy;
    private final MySequence dependentSequence;
    private volatile boolean alerted = false;
    private final MySequence cursorSequence;
    private final MySequencer sequencer;

    MyProcessingSequenceBarrier(
        final MySequencer sequencer,
        final MyWaitStrategy waitStrategy,
        final MySequence cursorSequence,
        final MySequence[] dependentSequences)
    {
        this.sequencer = sequencer;
        this.waitStrategy = waitStrategy;
        this.cursorSequence = cursorSequence;
        if (0 == dependentSequences.length)
        {
            dependentSequence = cursorSequence;
        }
        else
        {
            dependentSequence = new MyFixedSequenceGroup(dependentSequences);
        }
    }

    @Override
    public long waitFor(final long sequence) throws MyAlertException, InterruptedException, MyTimeoutException {
        checkAlert();

        long availableSequence = waitStrategy.waitFor(sequence, cursorSequence, dependentSequence, this);

        if (availableSequence < sequence)
        {
            return availableSequence;
        }

        return sequencer.getHighestPublishedSequence(sequence, availableSequence);
    }

    @Override
    public long getCursor() {
        return dependentSequence.get();
    }

    @Override
    public boolean isAlerted() {
        return alerted;
    }

    @Override
    public void alert() {
        alerted = true;
        waitStrategy.signalAllWhenBlocking();
    }

    @Override
    public void clearAlert() {
        alerted = false;
    }

    @Override
    public void checkAlert() throws MyAlertException {
        if (alerted)
        {
            throw MyAlertException.INSTANCE;
        }
    }
}
