package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/15
 */
public interface MyDataProvider<T> {
    T get(long sequence);
}
