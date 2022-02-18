package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/18
 */
public interface MyBatchStartAware {
    void onBatchStart(long batchSize);
}
