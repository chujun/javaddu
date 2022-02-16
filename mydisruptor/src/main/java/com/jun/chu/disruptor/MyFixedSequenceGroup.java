package com.jun.chu.disruptor;

import com.jun.chu.disruptor.util.MyUtil;

import java.util.Arrays;

/**
 * 表示这是一组Sequence
 * 只实现了get方法
 *
 * @author chujun
 * @date 2022/2/16
 */
public class MyFixedSequenceGroup extends MySequence {
    private final MySequence[] sequences;

    public MyFixedSequenceGroup(MySequence[] sequences) {
        this.sequences = Arrays.copyOf(sequences, sequences.length);
    }

    /**
     * 获取组中的最小值sequence
     *
     * @return
     */
    @Override
    public long get() {
        return MyUtil.getMinimumSequence(sequences);
    }

    @Override
    public String toString() {
        return Arrays.toString(sequences);
    }

    /**
     * Not supported.
     */
    @Override
    public void set(long value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    public boolean compareAndSet(long expectedValue, long newValue) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    public long incrementAndGet() {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    public long addAndGet(long increment) {
        throw new UnsupportedOperationException();
    }
}
