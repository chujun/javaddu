package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/17
 */
public interface MyEventReleaseAware {
    void setEventReleaser(MyEventReleaser eventReleaser);
}
