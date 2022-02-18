package com.jun.chu.disruptor;

import com.jun.chu.disruptor.dsl.MyProducerType;
import com.jun.chu.disruptor.util.MyUtil;
import sun.misc.Unsafe;

abstract class MyRingBufferPad {
    protected long p1, p2, p3, p4, p5, p6, p7;
}

abstract class MyRingBufferFields<E> extends MyRingBufferPad {
    //数组对象对齐pad长度
    private static final int BUFFER_PAD;
    //数组对象第一个元素地址
    private static final long REF_ARRAY_BASE;
    //公式 2^REF_ELEMENT_SHIFT=Object数组一个元素内存长度大小(8字节，还是4字节）和是否启动了class point size压缩有关系
    private static final int REF_ELEMENT_SHIFT;
    private static final Unsafe UNSAFE = MyUtil.getUnsafe();

    static {
        //UNSAFE.arrayIndexScale 返回数组中一个元素占用的大小
        final int scale = UNSAFE.arrayIndexScale(Object[].class);
        //保持一个等式：BUFFER_PAD << REF_ELEMENT_SHIFT=128
        if (4 == scale) {
            //2^2=4，64位jvm启动了class point size压缩
            REF_ELEMENT_SHIFT = 2;
        } else if (8 == scale) {
            //2^3=8，64位jvm没有启动class point size压缩
            REF_ELEMENT_SHIFT = 3;
        } else {
            throw new IllegalStateException("Unknown pointer size");
        }
        BUFFER_PAD = 128 / scale;
        //UNSAFE.arrayBaseOffset 返回数组中第一个元素的偏移地址
        // Including the buffer pad in the array base offset
        //TODO:cj 为什么需要加上后者BUFFER_PAD << REF_ELEMENT_SHIFT，去除掉有什么影响
        REF_ARRAY_BASE = UNSAFE.arrayBaseOffset(Object[].class) + (BUFFER_PAD << REF_ELEMENT_SHIFT);
    }

    //buffersize长度掩码，形如'1111111....'
    private final long indexMask;
    private final Object[] entries;
    protected final int bufferSize;
    protected final MySequencer sequencer;

    MyRingBufferFields(
        MyEventFactory<E> eventFactory,
        MySequencer sequencer) {
        this.sequencer = sequencer;
        this.bufferSize = sequencer.getBufferSize();

        if (bufferSize < 1) {
            throw new IllegalArgumentException("bufferSize must not be less than 1");
        }
        if (Integer.bitCount(bufferSize) != 1) {
            throw new IllegalArgumentException("bufferSize must be a power of 2");
        }

        this.indexMask = bufferSize - 1;
        //TODO:为何首位都要留白
        this.entries = new Object[sequencer.getBufferSize() + 2 * BUFFER_PAD];
        fill(eventFactory);
    }

    private void fill(MyEventFactory<E> eventFactory) {
        for (int i = 0; i < bufferSize; i++) {
            entries[BUFFER_PAD + i] = eventFactory.newInstance();
        }
    }

    /**
     * 放回指定sequence对应的环形数组元素
     * sequence & indexMask :sequence在底层数组结构的位置
     *
     * @param sequence
     * @return
     */
    protected final E elementAt(long sequence) {
        //获得给定对象的指定地址偏移量的值
        //TODO:cj 为何不直接用数组下标方式访问，二用UNSAFE方法访问，性能问题原因嘛？？jmh进行方法微测试
        return (E) UNSAFE.getObject(entries, REF_ARRAY_BASE + ((sequence & indexMask) << REF_ELEMENT_SHIFT));
    }


}

/**
 * @author chujun
 * @date 2022/2/16
 */
public final class MyRingBuffer<E> extends MyRingBufferFields<E> implements MyCursored, MyEventSequencer<E>, MyEventSink<E> {
    public static final long INITIAL_CURSOR_VALUE = MySequence.INITIAL_VALUE;
    protected long p1, p2, p3, p4, p5, p6, p7;

    MyRingBuffer(
        MyEventFactory<E> eventFactory,
        MySequencer sequencer) {
        super(eventFactory, sequencer);
    }

    public static <E> MyRingBuffer<E> createMultiProducer(
        MyEventFactory<E> factory,
        int bufferSize,
        MyWaitStrategy waitStrategy) {
        MyMultiProducerSequencer sequencer = new MyMultiProducerSequencer(bufferSize, waitStrategy);

        return new MyRingBuffer<E>(factory, sequencer);
    }

    public static <E> MyRingBuffer<E> createMultiProducer(MyEventFactory<E> factory, int bufferSize) {
        return createMultiProducer(factory, bufferSize, new MyBlockingWaitStrategy());
    }

    public static <E> MyRingBuffer<E> createSingleProducer(
        MyEventFactory<E> factory,
        int bufferSize,
        MyWaitStrategy waitStrategy) {
        MySingleProducerSequencer sequencer = new MySingleProducerSequencer(bufferSize, waitStrategy);

        return new MyRingBuffer<E>(factory, sequencer);
    }

    public static <E> MyRingBuffer<E> createSingleProducer(MyEventFactory<E> factory, int bufferSize) {
        return createSingleProducer(factory, bufferSize, new MyBlockingWaitStrategy());
    }

    public static <E> MyRingBuffer<E> create(
        MyProducerType producerType,
        MyEventFactory<E> factory,
        int bufferSize,
        MyWaitStrategy waitStrategy) {
        switch (producerType) {
            case SINGLE:
                return createSingleProducer(factory, bufferSize, waitStrategy);
            case MULTI:
                return createMultiProducer(factory, bufferSize, waitStrategy);
            default:
                throw new IllegalStateException(producerType.toString());
        }
    }

    @Override
    public E get(long sequence) {
        return elementAt(sequence);
    }

    /**
     * 事件发布标准流程
     * <pre>
     * long sequence = ringBuffer.next();
     * try {
     *     Event e = ringBuffer.get(sequence);
     *     // Do some work with the event.
     * } finally {
     *     ringBuffer.publish(sequence);
     * }
     * </pre>
     *
     * @return
     */
    @Override
    public long next() {
        return sequencer.next();
    }

    @Override
    public long next(int n) {
        return sequencer.next(n);
    }

    @Override
    public long tryNext() throws MyInsufficientCapacityException {
        return sequencer.tryNext();
    }

    @Override
    public long tryNext(int n) throws MyInsufficientCapacityException {
        return sequencer.tryNext(n);
    }

    @Deprecated
    public void resetTo(long sequence) {
        sequencer.claim(sequence);
        sequencer.publish(sequence);
    }

    public E claimAndGetPreallocated(long sequence) {
        sequencer.claim(sequence);
        return get(sequence);
    }

    @Deprecated
    public boolean isPublished(long sequence) {
        return sequencer.isAvailable(sequence);
    }

    public void addGatingSequences(MySequence... gatingSequences) {
        sequencer.addGatingSequences(gatingSequences);
    }

    public long getMinimumGatingSequence() {
        return sequencer.getMinimumSequence();
    }

    public boolean removeGatingSequence(MySequence sequence) {
        return sequencer.removeGatingSequence(sequence);
    }

    public MySequenceBarrier newBarrier(MySequence... sequencesToTrack) {
        return sequencer.newBarrier(sequencesToTrack);
    }

    //    public EventPoller<E> newPoller(Sequence... gatingSequences)
//    {
//        return sequencer.newPoller(this, gatingSequences);
//    }
    @Override
    public long getCursor() {
        return sequencer.getCursor();
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public boolean hasAvailableCapacity(int requiredCapacity) {
        return sequencer.hasAvailableCapacity(requiredCapacity);
    }

    @Override
    public void publishEvent(MyEventTranslator<E> translator) {
        final long sequence = sequencer.next();
        translateAndPublish(translator, sequence);
    }

    @Override
    public boolean tryPublishEvent(MyEventTranslator<E> translator) {
        try {
            final long sequence = sequencer.tryNext();
            translateAndPublish(translator, sequence);
            return true;
        } catch (MyInsufficientCapacityException e) {
            return false;
        }
    }

    @Override
    public <A> void publishEvent(MyEventTranslatorOneArg<E, A> translator, A arg0) {
        final long sequence = sequencer.next();
        translateAndPublish(translator, sequence, arg0);
    }

    @Override
    public <A> boolean tryPublishEvent(MyEventTranslatorOneArg<E, A> translator, A arg0) {
        try {
            final long sequence = sequencer.tryNext();
            translateAndPublish(translator, sequence, arg0);
            return true;
        } catch (MyInsufficientCapacityException e) {
            return false;
        }
    }

    @Override
    public <A, B> void publishEvent(MyEventTranslatorTwoArg<E, A, B> translator, A arg0, B arg1) {
        final long sequence = sequencer.next();
        translateAndPublish(translator, sequence, arg0, arg1);
    }

    @Override
    public <A, B> boolean tryPublishEvent(MyEventTranslatorTwoArg<E, A, B> translator, A arg0, B arg1) {
        try {
            final long sequence = sequencer.tryNext();
            translateAndPublish(translator, sequence, arg0, arg1);
            return true;
        } catch (MyInsufficientCapacityException e) {
            return false;
        }
    }

    @Override
    public <A, B, C> void publishEvent(MyEventTranslatorThreeArg<E, A, B, C> translator, A arg0, B arg1, C arg2) {
        final long sequence = sequencer.next();
        translateAndPublish(translator, sequence, arg0, arg1, arg2);
    }

    @Override
    public <A, B, C> boolean tryPublishEvent(MyEventTranslatorThreeArg<E, A, B, C> translator, A arg0, B arg1, C arg2) {
        try {
            final long sequence = sequencer.tryNext();
            translateAndPublish(translator, sequence, arg0, arg1, arg2);
            return true;
        } catch (MyInsufficientCapacityException e) {
            return false;
        }
    }

    @Override
    public void publishEvent(MyEventTranslatorVararg<E> translator, Object... args) {
        final long sequence = sequencer.next();
        translateAndPublish(translator, sequence, args);
    }

    @Override
    public boolean tryPublishEvent(MyEventTranslatorVararg<E> translator, Object... args) {
        try {
            final long sequence = sequencer.tryNext();
            translateAndPublish(translator, sequence, args);
            return true;
        } catch (MyInsufficientCapacityException e) {
            return false;
        }
    }

    @Override
    public void publishEvents(MyEventTranslator<E>[] translators) {
        publishEvents(translators, 0, translators.length);
    }

    @Override
    public void publishEvents(MyEventTranslator<E>[] translators, int batchStartsAt, int batchSize) {
        checkBounds(translators, batchStartsAt, batchSize);
        final long finalSequence = sequencer.next(batchSize);
        translateAndPublishBatch(translators, batchStartsAt, batchSize, finalSequence);
    }

    @Override
    public boolean tryPublishEvents(MyEventTranslator<E>[] translators) {
        return tryPublishEvents(translators, 0, translators.length);
    }

    @Override
    public boolean tryPublishEvents(MyEventTranslator<E>[] translators, int batchStartsAt, int batchSize) {
        checkBounds(translators, batchStartsAt, batchSize);
        try {
            final long finalSequence = sequencer.tryNext(batchSize);
            translateAndPublishBatch(translators, batchStartsAt, batchSize, finalSequence);
            return true;
        } catch (MyInsufficientCapacityException e) {
            return false;
        }
    }

    @Override
    public <A> void publishEvents(MyEventTranslatorOneArg<E, A> translator, A[] arg0) {
        publishEvents(translator, 0, arg0.length, arg0);
    }

    @Override
    public <A> void publishEvents(MyEventTranslatorOneArg<E, A> translator, int batchStartsAt, int batchSize, A[] arg0) {
        checkBounds(arg0, batchStartsAt, batchSize);
        final long finalSequence = sequencer.next(batchSize);
        translateAndPublishBatch(translator, arg0, batchStartsAt, batchSize, finalSequence);
    }

    @Override
    public <A> boolean tryPublishEvents(MyEventTranslatorOneArg<E, A> translator, A[] arg0) {
        return tryPublishEvents(translator, 0, arg0.length, arg0);
    }

    @Override
    public <A> boolean tryPublishEvents(
        MyEventTranslatorOneArg<E, A> translator, int batchStartsAt, int batchSize, A[] arg0) {
        checkBounds(arg0, batchStartsAt, batchSize);
        try {
            final long finalSequence = sequencer.tryNext(batchSize);
            translateAndPublishBatch(translator, arg0, batchStartsAt, batchSize, finalSequence);
            return true;
        } catch (MyInsufficientCapacityException e) {
            return false;
        }
    }

    @Override
    public <A, B> void publishEvents(MyEventTranslatorTwoArg<E, A, B> translator, A[] arg0, B[] arg1) {
        publishEvents(translator, 0, arg0.length, arg0, arg1);
    }

    @Override
    public <A, B> void publishEvents(
        MyEventTranslatorTwoArg<E, A, B> translator, int batchStartsAt, int batchSize, A[] arg0, B[] arg1) {
        checkBounds(arg0, arg1, batchStartsAt, batchSize);
        final long finalSequence = sequencer.next(batchSize);
        translateAndPublishBatch(translator, arg0, arg1, batchStartsAt, batchSize, finalSequence);
    }

    @Override
    public <A, B> boolean tryPublishEvents(MyEventTranslatorTwoArg<E, A, B> translator, A[] arg0, B[] arg1) {
        return tryPublishEvents(translator, 0, arg0.length, arg0, arg1);
    }

    @Override
    public <A, B> boolean tryPublishEvents(
        MyEventTranslatorTwoArg<E, A, B> translator, int batchStartsAt, int batchSize, A[] arg0, B[] arg1) {
        checkBounds(arg0, arg1, batchStartsAt, batchSize);
        try {
            final long finalSequence = sequencer.tryNext(batchSize);
            translateAndPublishBatch(translator, arg0, arg1, batchStartsAt, batchSize, finalSequence);
            return true;
        } catch (MyInsufficientCapacityException e) {
            return false;
        }
    }

    @Override
    public <A, B, C> void publishEvents(MyEventTranslatorThreeArg<E, A, B, C> translator, A[] arg0, B[] arg1, C[] arg2) {
        publishEvents(translator, 0, arg0.length, arg0, arg1, arg2);
    }

    @Override
    public <A, B, C> void publishEvents(
        MyEventTranslatorThreeArg<E, A, B, C> translator, int batchStartsAt, int batchSize, A[] arg0, B[] arg1, C[] arg2) {
        checkBounds(arg0, arg1, arg2, batchStartsAt, batchSize);
        final long finalSequence = sequencer.next(batchSize);
        translateAndPublishBatch(translator, arg0, arg1, arg2, batchStartsAt, batchSize, finalSequence);
    }

    @Override
    public <A, B, C> boolean tryPublishEvents(
        MyEventTranslatorThreeArg<E, A, B, C> translator, A[] arg0, B[] arg1, C[] arg2) {
        return tryPublishEvents(translator, 0, arg0.length, arg0, arg1, arg2);
    }

    @Override
    public <A, B, C> boolean tryPublishEvents(
        MyEventTranslatorThreeArg<E, A, B, C> translator, int batchStartsAt, int batchSize, A[] arg0, B[] arg1, C[] arg2) {
        checkBounds(arg0, arg1, arg2, batchStartsAt, batchSize);
        try {
            final long finalSequence = sequencer.tryNext(batchSize);
            translateAndPublishBatch(translator, arg0, arg1, arg2, batchStartsAt, batchSize, finalSequence);
            return true;
        } catch (MyInsufficientCapacityException e) {
            return false;
        }
    }

    @Override
    public void publishEvents(MyEventTranslatorVararg<E> translator, Object[]... args) {
        publishEvents(translator, 0, args.length, args);
    }

    @Override
    public void publishEvents(MyEventTranslatorVararg<E> translator, int batchStartsAt, int batchSize, Object[]... args) {
        checkBounds(batchStartsAt, batchSize, args);
        final long finalSequence = sequencer.next(batchSize);
        translateAndPublishBatch(translator, batchStartsAt, batchSize, finalSequence, args);
    }

    @Override
    public boolean tryPublishEvents(MyEventTranslatorVararg<E> translator, Object[]... args) {
        return tryPublishEvents(translator, 0, args.length, args);
    }

    @Override
    public boolean tryPublishEvents(
        MyEventTranslatorVararg<E> translator, int batchStartsAt, int batchSize, Object[]... args) {
        checkBounds(args, batchStartsAt, batchSize);
        try {
            final long finalSequence = sequencer.tryNext(batchSize);
            translateAndPublishBatch(translator, batchStartsAt, batchSize, finalSequence, args);
            return true;
        } catch (MyInsufficientCapacityException e) {
            return false;
        }
    }

    @Override
    public void publish(long sequence) {
        sequencer.publish(sequence);
    }

    @Override
    public void publish(long lo, long hi) {
        sequencer.publish(lo, hi);
    }

    @Override
    public long remainingCapacity() {
        return sequencer.remainingCapacity();
    }

    private <A> void checkBounds(final A[] arg0, final int batchStartsAt, final int batchSize) {
        checkBatchSizing(batchStartsAt, batchSize);
        batchOverRuns(arg0, batchStartsAt, batchSize);
    }

    private void checkBounds(final MyEventTranslator<E>[] translators, final int batchStartsAt, final int batchSize) {
        checkBatchSizing(batchStartsAt, batchSize);
        batchOverRuns(translators, batchStartsAt, batchSize);
    }

    private <A, B> void checkBounds(final A[] arg0, final B[] arg1, final int batchStartsAt, final int batchSize) {
        checkBatchSizing(batchStartsAt, batchSize);
        batchOverRuns(arg0, batchStartsAt, batchSize);
        batchOverRuns(arg1, batchStartsAt, batchSize);
    }

    private <A, B, C> void checkBounds(
        final A[] arg0, final B[] arg1, final C[] arg2, final int batchStartsAt, final int batchSize) {
        checkBatchSizing(batchStartsAt, batchSize);
        batchOverRuns(arg0, batchStartsAt, batchSize);
        batchOverRuns(arg1, batchStartsAt, batchSize);
        batchOverRuns(arg2, batchStartsAt, batchSize);
    }

    private void checkBounds(final int batchStartsAt, final int batchSize, final Object[][] args) {
        checkBatchSizing(batchStartsAt, batchSize);
        batchOverRuns(args, batchStartsAt, batchSize);
    }

    private void checkBatchSizing(int batchStartsAt, int batchSize) {
        if (batchStartsAt < 0 || batchSize < 0) {
            throw new IllegalArgumentException("Both batchStartsAt and batchSize must be positive but got: batchStartsAt " + batchStartsAt + " and batchSize " + batchSize);
        } else if (batchSize > bufferSize) {
            throw new IllegalArgumentException("The ring buffer cannot accommodate " + batchSize + " it only has space for " + bufferSize + " entities.");
        }
    }

    private <A> void batchOverRuns(final A[] arg0, final int batchStartsAt, final int batchSize) {
        if (batchStartsAt + batchSize > arg0.length) {
            throw new IllegalArgumentException(
                "A batchSize of: " + batchSize +
                    " with batchStatsAt of: " + batchStartsAt +
                    " will overrun the available number of arguments: " + (arg0.length - batchStartsAt));
        }
    }

    private void translateAndPublish(MyEventTranslator<E> translator, long sequence) {
        try {
            translator.translateTo(get(sequence), sequence);
        } finally {
            sequencer.publish(sequence);
        }
    }

    private <A> void translateAndPublish(MyEventTranslatorOneArg<E, A> translator, long sequence, A arg0) {
        try {
            translator.translateTo(get(sequence), sequence, arg0);
        } finally {
            sequencer.publish(sequence);
        }
    }

    private <A, B> void translateAndPublish(MyEventTranslatorTwoArg<E, A, B> translator, long sequence, A arg0, B arg1) {
        try {
            translator.translateTo(get(sequence), sequence, arg0, arg1);
        } finally {
            sequencer.publish(sequence);
        }
    }

    private <A, B, C> void translateAndPublish(
        MyEventTranslatorThreeArg<E, A, B, C> translator, long sequence,
        A arg0, B arg1, C arg2) {
        try {
            translator.translateTo(get(sequence), sequence, arg0, arg1, arg2);
        } finally {
            sequencer.publish(sequence);
        }
    }

    private void translateAndPublish(MyEventTranslatorVararg<E> translator, long sequence, Object... args) {
        try {
            translator.translateTo(get(sequence), sequence, args);
        } finally {
            sequencer.publish(sequence);
        }
    }

    private void translateAndPublishBatch(
        final MyEventTranslator<E>[] translators, int batchStartsAt,
        final int batchSize, final long finalSequence) {
        final long initialSequence = finalSequence - (batchSize - 1);
        try {
            long sequence = initialSequence;
            final int batchEndsAt = batchStartsAt + batchSize;
            for (int i = batchStartsAt; i < batchEndsAt; i++) {
                final MyEventTranslator<E> translator = translators[i];
                translator.translateTo(get(sequence), sequence++);
            }
        } finally {
            sequencer.publish(initialSequence, finalSequence);
        }
    }

    private <A> void translateAndPublishBatch(
        final MyEventTranslatorOneArg<E, A> translator, final A[] arg0,
        int batchStartsAt, final int batchSize, final long finalSequence) {
        final long initialSequence = finalSequence - (batchSize - 1);
        try {
            long sequence = initialSequence;
            final int batchEndsAt = batchStartsAt + batchSize;
            for (int i = batchStartsAt; i < batchEndsAt; i++) {
                translator.translateTo(get(sequence), sequence++, arg0[i]);
            }
        } finally {
            sequencer.publish(initialSequence, finalSequence);
        }
    }

    private <A, B> void translateAndPublishBatch(
        final MyEventTranslatorTwoArg<E, A, B> translator, final A[] arg0,
        final B[] arg1, int batchStartsAt, int batchSize,
        final long finalSequence) {
        final long initialSequence = finalSequence - (batchSize - 1);
        try {
            long sequence = initialSequence;
            final int batchEndsAt = batchStartsAt + batchSize;
            for (int i = batchStartsAt; i < batchEndsAt; i++) {
                translator.translateTo(get(sequence), sequence++, arg0[i], arg1[i]);
            }
        } finally {
            sequencer.publish(initialSequence, finalSequence);
        }
    }

    private <A, B, C> void translateAndPublishBatch(
        final MyEventTranslatorThreeArg<E, A, B, C> translator,
        final A[] arg0, final B[] arg1, final C[] arg2, int batchStartsAt,
        final int batchSize, final long finalSequence) {
        final long initialSequence = finalSequence - (batchSize - 1);
        try {
            long sequence = initialSequence;
            final int batchEndsAt = batchStartsAt + batchSize;
            for (int i = batchStartsAt; i < batchEndsAt; i++) {
                translator.translateTo(get(sequence), sequence++, arg0[i], arg1[i], arg2[i]);
            }
        } finally {
            sequencer.publish(initialSequence, finalSequence);
        }
    }

    private void translateAndPublishBatch(
        final MyEventTranslatorVararg<E> translator, int batchStartsAt,
        final int batchSize, final long finalSequence, final Object[][] args) {
        final long initialSequence = finalSequence - (batchSize - 1);
        try {
            long sequence = initialSequence;
            final int batchEndsAt = batchStartsAt + batchSize;
            for (int i = batchStartsAt; i < batchEndsAt; i++) {
                translator.translateTo(get(sequence), sequence++, args[i]);
            }
        } finally {
            sequencer.publish(initialSequence, finalSequence);
        }
    }

    @Override
    public String toString()
    {
        return "MyRingBuffer{" +
            "bufferSize=" + bufferSize +
            ", sequencer=" + sequencer +
            "}";
    }
}
