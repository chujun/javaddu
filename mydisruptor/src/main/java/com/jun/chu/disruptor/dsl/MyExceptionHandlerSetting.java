package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MyBatchEventProcessor;
import com.jun.chu.disruptor.MyEventHandler;
import com.jun.chu.disruptor.MyEventProcessor;
import com.jun.chu.disruptor.MyExceptionHandler;

/**
 * @author chujun
 * @date 2022/2/19
 */
public class MyExceptionHandlerSetting<T> {
    private final MyEventHandler<T> eventHandler;
    private final MyConsumerRepository<T> consumerRepository;

    MyExceptionHandlerSetting(
        final MyEventHandler<T> eventHandler,
        final MyConsumerRepository<T> consumerRepository)
    {
        this.eventHandler = eventHandler;
        this.consumerRepository = consumerRepository;
    }

    public void with(MyExceptionHandler<? super T> exceptionHandler)
    {
        final MyEventProcessor eventProcessor = consumerRepository.getEventProcessorFor(eventHandler);
        if (eventProcessor instanceof MyBatchEventProcessor)
        {
            ((MyBatchEventProcessor<T>) eventProcessor).setExceptionHandler(exceptionHandler);
            consumerRepository.getBarrierFor(eventHandler).alert();
        }
        else
        {
            throw new RuntimeException(
                "EventProcessor: " + eventProcessor + " is not a BatchEventProcessor " +
                    "and does not support exception handlers");
        }
    }
}
