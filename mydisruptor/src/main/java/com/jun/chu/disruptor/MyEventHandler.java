package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/17
 */
public interface MyEventHandler<T> {
    void onEvent(T event, long sequence, boolean endOfBatch) throws Exception;
}
