package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/18
 */
public interface MySequenceReportingEventHandler<T> extends MyEventHandler<T> {
    void setSequenceCallback(MySequence sequenceCallback);
}
