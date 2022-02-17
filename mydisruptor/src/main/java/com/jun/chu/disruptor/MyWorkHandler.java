package com.jun.chu.disruptor;

/**
 * api用户实现
 *
 * @author chujun
 * @date 2022/2/17
 */
public interface MyWorkHandler<T> {
    void onEvent(T event) throws Exception;
}
