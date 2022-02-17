package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/17
 */
public interface MyTimeoutHandler {
    void onTimeout(long sequence) throws Exception;
}
