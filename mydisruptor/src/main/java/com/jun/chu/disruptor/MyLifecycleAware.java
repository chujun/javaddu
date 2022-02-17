package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/17
 */
public interface MyLifecycleAware {
    void onStart();

    void onShutdown();
}
