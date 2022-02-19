package com.jun.chu.disruptor.dsl.stubs;

import com.jun.chu.disruptor.MyEventHandler;
import com.jun.chu.disruptor.supports.TestEvent;

/**
 * @author chujun
 * @date 2022/2/19
 */
public class SleepingEventHandler  implements MyEventHandler<TestEvent> {
    @Override
    public void onEvent(final TestEvent entry, final long sequence, final boolean endOfBatch) throws Exception
    {
        Thread.sleep(1000);
    }
}
