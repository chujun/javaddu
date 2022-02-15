package com.jun.chu.disruptor;

/**
 * 被RingBuffer调用用于预分配所有事件来填充RingBuffer
 * @author chujun
 * @date 2022/2/15
 */
public interface MyEventFactory<T> {
    /*
     * Implementations should instantiate an event object, with all memory already allocated where possible.
     */
    T newInstance();
}
