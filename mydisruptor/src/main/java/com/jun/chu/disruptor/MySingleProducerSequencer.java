package com.jun.chu.disruptor;


import com.jun.chu.disruptor.util.MyUtil;

import java.util.concurrent.locks.LockSupport;

abstract class MySingleProducerSequencerPad extends MyAbstractSequencer {
    protected long p1, p2, p3, p4, p5, p6, p7;

    MySingleProducerSequencerPad(int bufferSize, MyWaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
    }
}

abstract class MySingleProducerSequencerFields extends MySingleProducerSequencerPad {
    MySingleProducerSequencerFields(int bufferSize, MyWaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
    }

    /**
     * Set to -1 as sequence starting point
     */
    long nextValue = MySequence.INITIAL_VALUE;
    //TODO:cj 这个字段的作用是什么？ 维护消费者集合消费最低的sequence值
    long cachedValue = MySequence.INITIAL_VALUE;
}

/**
 * 多线程不安全，多线程发布者请使用MyMultiProducerSequencer
 *
 * @author chujun
 * @date 2022/2/18
 */
public class MySingleProducerSequencer extends MySingleProducerSequencerFields {
    protected long p1, p2, p3, p4, p5, p6, p7;

    public MySingleProducerSequencer(int bufferSize, MyWaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
    }

    @Override
    public boolean hasAvailableCapacity(int requiredCapacity) {
        return hasAvailableCapacity(requiredCapacity, false);
    }

    private boolean hasAvailableCapacity(int requiredCapacity, boolean doStore) {
        long nextValue = this.nextValue;
        //最高可发布sequence值阈值<Min(消费者集合已消费的最低sequence值)+bufferSize，不能套娃
        long wrapPoint = (nextValue + requiredCapacity) - bufferSize;
        long cachedGatingSequence = this.cachedValue;
        //貌似不太可能满足cachedGatingSequence > nextValue 条件
        if (wrapPoint > cachedGatingSequence || cachedGatingSequence > nextValue) {
            //TODO:cj doStore传true的时候作用
            if (doStore) {
                cursor.setVolatile(nextValue);  // StoreLoad fence
            }

            long minSequence = MyUtil.getMinimumSequence(gatingSequences, nextValue);
            //更新最新的消费者集合消费的sequence点
            this.cachedValue = minSequence;
            //可发布阈值还是大于消费者集合已消费sequence最低值，表示申请可用容量不足,则返回false
            if (wrapPoint > minSequence) {
                return false;
            }
        }

        return true;
    }

    @Override
    public long next() {
        return next(1);
    }

    @Override
    public long next(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be > 0");
        }

        long nextValue = this.nextValue;

        long nextSequence = nextValue + n;
        //最高可发布sequence值阈值A<Min(消费者集合已消费的最低sequence值)+bufferSize，不能套娃。nextSequence表示阈值A
        long wrapPoint = nextSequence - bufferSize;
        long cachedGatingSequence = this.cachedValue;

        if (wrapPoint > cachedGatingSequence || cachedGatingSequence > nextValue) {
            //供其他消费者线程可见，而非生产者线程，因为这就是个单生产者线程
            cursor.setVolatile(nextValue);  // StoreLoad fence

            long minSequence;
            //如果消费速度过慢，导致最低已消费sequence更新太慢， 则无限循环等待消费者完成最低sequence消费，使得可供生产者生产事件
            while (wrapPoint > (minSequence = MyUtil.getMinimumSequence(gatingSequences, nextValue))) {
                LockSupport.parkNanos(1L); // TODO: Use waitStrategy to spin?
            }

            this.cachedValue = minSequence;
        }

        this.nextValue = nextSequence;

        return nextSequence;
    }

    @Override
    public long tryNext() throws MyInsufficientCapacityException {
        return tryNext(1);
    }

    @Override
    public long tryNext(int n) throws MyInsufficientCapacityException {
        if (n < 1) {
            throw new IllegalArgumentException("n must be > 0");
        }

        if (!hasAvailableCapacity(n, true)) {
            throw MyInsufficientCapacityException.INSTANCE;
        }

        long nextSequence = this.nextValue += n;

        return nextSequence;
    }

    @Override
    public long remainingCapacity() {
        long nextValue = this.nextValue;
        //最低已消费sequence值
        long consumed = MyUtil.getMinimumSequence(gatingSequences, nextValue);
        //已发布sequence值
        long produced = nextValue;
        return getBufferSize() - (produced - consumed);
    }

    @Override
    public void claim(long sequence) {
        this.nextValue = sequence;
    }

    @Override
    public void publish(long sequence) {
        cursor.set(sequence);
        waitStrategy.signalAllWhenBlocking();
    }

    @Override
    public void publish(long lo, long hi) {
        publish(hi);
    }

    @Override
    public boolean isAvailable(long sequence) {
        return sequence <= cursor.get();
    }

    @Override
    public long getHighestPublishedSequence(long lowerBound, long availableSequence) {
        return availableSequence;
    }
}
