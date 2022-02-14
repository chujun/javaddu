package com.jun.chu.java.disruptor.sample;

import com.lmax.disruptor.EventFactory;

/**
 * 定义事件工厂
 *
 * @author chujun
 * @date 2022/2/14
 */
public class MyEventFactory implements EventFactory<MyEvent> {
    @Override
    public MyEvent newInstance() {
        return new MyEvent();
    }
}
