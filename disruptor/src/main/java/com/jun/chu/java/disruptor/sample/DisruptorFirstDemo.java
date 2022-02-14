package com.jun.chu.java.disruptor.sample;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @see 阿里云开发者社区 Disruptor深入解读 https://developer.aliyun.com/article/553297
 * @author chujun
 * @date 2022/2/14
 */
public class DisruptorFirstDemo {
    public static void main(String[] args) {
        EventFactory<MyEvent> myEventFactory = new MyEventFactory();
        Executor executor = Executors.newCachedThreadPool();
        int ringBufferSize = 32;

        Disruptor<MyEvent> disruptor = new Disruptor<MyEvent>(myEventFactory, ringBufferSize, executor, ProducerType.SINGLE, new BlockingWaitStrategy());
        EventHandler<MyEvent> b = new MyEventHandlerB();
        EventHandler<MyEvent> c = new MyEventHandlerC();
        EventHandler<MyEvent> d = new MyEventHandlerD();
        //消费者依赖关系图
        SequenceBarrier sequenceBarrier2 = disruptor.handleEventsWith(b, c).asSequenceBarrier();
        BatchEventProcessor processord = new BatchEventProcessor(disruptor.getRingBuffer(), sequenceBarrier2, d);
        disruptor.handleEventsWith(processord);
        // 此行能代替上两行的程序逻辑，消费者依赖关系图
        //disruptor.after(b, c).handleEventsWith(d);
        // 启动Disruptor
        RingBuffer<MyEvent> ringBuffer = disruptor.start();
        for (int i = 0; i < 10; i++) {
            // 申请位置
            long sequence = ringBuffer.next();
            try {

                MyEvent myEvent = ringBuffer.get(sequence);
                // 放置数据
                myEvent.setValue(i);
            } finally {
                // 提交，如果不提交完成事件会一直阻塞
                ringBuffer.publish(sequence);
            }
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
        disruptor.shutdown();
    }
}
