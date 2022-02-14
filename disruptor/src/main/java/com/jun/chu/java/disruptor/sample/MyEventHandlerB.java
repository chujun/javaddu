package com.jun.chu.java.disruptor.sample;

import com.lmax.disruptor.EventHandler;

/**
 * @author chujun
 * @date 2022/2/14
 */
public class MyEventHandlerB implements EventHandler<MyEvent> {

    @Override
    public void onEvent(final MyEvent event, final long sequence, final boolean endOfBatch) throws Exception {
        System.out.println("Comsume Event B : " + event.getValue());
    }
}
