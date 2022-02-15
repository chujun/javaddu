package com.jun.chu.disruptor.dsl;

/**
 * Defines producer types to support creation of RingBuffer with correct sequencer and publisher.
 * 定义生产者类型，用来支持创建RingBuffer的Sequencer和publisher
 * @author chujun
 * @date 2022/2/15
 */
public enum MyProducerType {
    /**
     * Create a RingBuffer with a single event publisher to the RingBuffer
     */
    SINGLE,

    /**
     * Create a RingBuffer supporting multiple event publishers to the one RingBuffer
     */
    MULTI
}
