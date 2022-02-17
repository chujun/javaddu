package com.jun.chu.disruptor;

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
        return (E) UNSAFE.getObject(entries, REF_ARRAY_BASE + ((sequence & indexMask) << REF_ELEMENT_SHIFT));
    }


}

/**
 * @author chujun
 * @date 2022/2/16
 */
public final class MyRingBuffer<E> extends MyRingBufferFields<E> implements MyCursored, MyEventSequencer<E>, MyEventSink<E> {
}
