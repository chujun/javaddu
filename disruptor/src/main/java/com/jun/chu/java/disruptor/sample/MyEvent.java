package com.jun.chu.java.disruptor.sample;

/**
 * 定义用户事件
 *
 * @author chujun
 * @date 2022/2/14
 */
public class MyEvent {
    private long value;

    public MyEvent() {
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
