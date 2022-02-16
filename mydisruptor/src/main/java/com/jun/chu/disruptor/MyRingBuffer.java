package com.jun.chu.disruptor;

abstract class MyRingBufferPad {
    protected long p1, p2, p3, p4, p5, p6, p7;
}

abstract class MyRingBufferFields<E> extends MyRingBufferPad {
    //TODO:cj to be done
}

/**
 * @author chujun
 * @date 2022/2/16
 */
public final class MyRingBuffer<E> extends MyRingBufferFields<E> implements MyCursored, MyEventSequencer<E>, MyEventSink<E> {
}
