package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MyEventProcessor;
import com.jun.chu.disruptor.MyRingBuffer;
import com.jun.chu.disruptor.MySequence;

/**
 * @author chujun
 * @date 2022/2/19
 */
public interface MyEventProcessorFactory<T> {
    MyEventProcessor createEventProcessor(MyRingBuffer<T> ringBuffer, MySequence[] barrierSequences);
}
