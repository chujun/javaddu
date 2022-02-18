package com.jun.chu.disruptor;

import com.jun.chu.disruptor.util.MyUtil;
import sun.misc.Unsafe;

import java.util.concurrent.locks.LockSupport;

/**
 * 线程安全
 *
 * @author chujun
 * @date 2022/2/18
 */
public final class MyMultiProducerSequencer extends MyAbstractSequencer {
    private static final Unsafe UNSAFE = MyUtil.getUnsafe();
    //int[]数组对象第一个元素内存偏移量
    private static final long BASE = UNSAFE.arrayBaseOffset(int[].class);
    //int[]数组对象一个元素内存字节大小
    private static final long SCALE = UNSAFE.arrayIndexScale(int[].class);
    //消费者集合已消费最低sequence
    private final MySequence gatingSequenceCache = new MySequence(MySequencer.INITIAL_CURSOR_VALUE);

    //可用性buffer数组用于ringbuffer状态跟踪，这么设计不是很占空间嘛
    private final int[] availableBuffer;
    private final int indexMask;
    private final int indexShift;


    public MyMultiProducerSequencer(int bufferSize, final MyWaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
        availableBuffer = new int[bufferSize];
        indexMask = bufferSize - 1;
        indexShift = MyUtil.log2(bufferSize);
        initialiseAvailableBuffer();
    }

    @Override
    public boolean hasAvailableCapacity(final int requiredCapacity) {
        return hasAvailableCapacity(gatingSequences, requiredCapacity, cursor.get());
    }

    private boolean hasAvailableCapacity(MySequence[] gatingSequences, final int requiredCapacity, long cursorValue) {
        long wrapPoint = (cursorValue + requiredCapacity) - bufferSize;
        long cachedGatingSequence = gatingSequenceCache.get();

        if (wrapPoint > cachedGatingSequence || cachedGatingSequence > cursorValue) {
            long minSequence = MyUtil.getMinimumSequence(gatingSequences, cursorValue);
            gatingSequenceCache.set(minSequence);

            if (wrapPoint > minSequence) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void claim(long sequence) {
        cursor.set(sequence);
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

        long current;
        long next;

        do {
            current = cursor.get();
            next = current + n;
            //最高可发布sequence值阈值A<Min(消费者集合已消费的最低sequence值)+bufferSize，不能套娃。nextSequence表示阈值A
            long wrapPoint = next - bufferSize;
            long cachedGatingSequence = gatingSequenceCache.get();

            if (wrapPoint > cachedGatingSequence || cachedGatingSequence > current) {
                long gatingSequence = MyUtil.getMinimumSequence(gatingSequences, current);

                if (wrapPoint > gatingSequence) {
                    LockSupport.parkNanos(1); // TODO, should we spin based on the wait strategy?
                    continue;
                }
                //更新消费者最低已消费sequence
                gatingSequenceCache.set(gatingSequence);
            }
            //CAS原子更新，因为可能多个生产者线程并发修改current
            else if (cursor.compareAndSet(current, next)) {
                break;
            }
        }
        while (true);

        return next;
    }

    /**
     * @see MySequencer#tryNext()
     */
    @Override
    public long tryNext() throws MyInsufficientCapacityException {
        return tryNext(1);
    }

    @Override
    public long tryNext(int n) throws MyInsufficientCapacityException {
        if (n < 1) {
            throw new IllegalArgumentException("n must be > 0");
        }

        long current;
        long next;

        do {
            current = cursor.get();
            next = current + n;

            if (!hasAvailableCapacity(gatingSequences, n, current)) {
                throw MyInsufficientCapacityException.INSTANCE;
            }
        }
        while (!cursor.compareAndSet(current, next));

        return next;
    }

    @Override
    public long remainingCapacity() {
        //最低已消费sequence值
        long consumed = MyUtil.getMinimumSequence(gatingSequences, cursor.get());
        //已发布sequence值
        long produced = cursor.get();
        return getBufferSize() - (produced - consumed);
    }

    private void initialiseAvailableBuffer() {
        for (int i = availableBuffer.length - 1; i != 0; i--) {
            setAvailableBufferValue(i, -1);
        }

        setAvailableBufferValue(0, -1);
    }

    @Override
    public void publish(final long sequence) {
        setAvailable(sequence);
        waitStrategy.signalAllWhenBlocking();
    }

    @Override
    public void publish(long lo, long hi) {
        for (long l = lo; l <= hi; l++) {
            setAvailable(l);
        }
        waitStrategy.signalAllWhenBlocking();
    }

    /**
     * 参见源码大段doc
     * 这儿没有使用gating sequences向前移动，而换了一种数组buffer替代表示
     */
    private void setAvailable(final long sequence) {
        setAvailableBufferValue(calculateIndex(sequence), calculateAvailabilityFlag(sequence));
    }

    /**
     * 设置指定数组buffer下标为flag值
     */
    private void setAvailableBufferValue(int index, int flag) {
        long bufferAddress = (index * SCALE) + BASE;
        UNSAFE.putOrderedInt(availableBuffer, bufferAddress, flag);
    }

    @Override
    public boolean isAvailable(long sequence) {
        //buffer数组下标
        int index = calculateIndex(sequence);
        //计算sequence对应的flag值
        int flag = calculateAvailabilityFlag(sequence);
        //buffer数组下标偏移量
        long bufferAddress = (index * SCALE) + BASE;
        return UNSAFE.getIntVolatile(availableBuffer, bufferAddress) == flag;
    }

    @Override
    public long getHighestPublishedSequence(long lowerBound, long availableSequence) {
        for (long sequence = lowerBound; sequence <= availableSequence; sequence++) {
            if (!isAvailable(sequence)) {
                return sequence - 1;
            }
        }

        return availableSequence;
    }


    private int calculateAvailabilityFlag(final long sequence) {
        //无符号右移
        return (int) (sequence >>> indexShift);
    }

    private int calculateIndex(final long sequence) {
        return ((int) sequence) & indexMask;
    }
}
