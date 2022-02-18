package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/17
 */
public interface MySequencer extends MyCursored, MySequenced {
    /**
     * 初始化游标值：-1
     */
    long INITIAL_CURSOR_VALUE = -1L;

    /**
     * Claim a specific sequence.  Only used if initialising the ring buffer to
     * a specific value.
     */
    void claim(long sequence);

    boolean isAvailable(long sequence);

    void addGatingSequences(MySequence... gatingSequences);

    boolean removeGatingSequence(MySequence sequence);

    MySequenceBarrier newBarrier(MySequence... sequencesToTrack);

    long getMinimumSequence();

    /**
     * TODO:cj 这个方法值得重点探究下
     */
    long getHighestPublishedSequence(long nextSequence, long availableSequence);

    //<T> EventPoller<T> newPoller(DataProvider<T> provider, Sequence... gatingSequences);
}
