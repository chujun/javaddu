package com.jun.chu.disruptor;

import com.jun.chu.disruptor.util.MyThreadHints;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用lock和condition变量实现屏障等待
 *
 * @author chujun
 * @date 2022/2/16
 */
public class MyBlockingWaitStrategy implements MyWaitStrategy {
    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = lock.newCondition();

    @Override
    public long waitFor(final long sequence, final MySequence cursor, final MySequence dependentSequence, final MySequenceBarrier barrier) throws MyAlertException, InterruptedException, MyTimeoutException {
        long availableSequence;
        //生产者没有发布最新事件
        if (cursor.get() < sequence) {
            lock.lock();
            try {
                //加锁是为了等待被唤醒
                while (cursor.get() < sequence) {
                    barrier.checkAlert();
                    processorNotifyCondition.await();
                }
            } finally {
                lock.unlock();
            }
        }

        //availableSequence 从依赖的消费者组获取最小的sequence
        while ((availableSequence = dependentSequence.get()) < sequence) {
            barrier.checkAlert();
            MyThreadHints.onSpinWait();
        }

        return availableSequence;

    }

    @Override
    public void signalAllWhenBlocking() {
        lock.lock();
        try {
            processorNotifyCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "BlockingWaitStrategy{" +
            "processorNotifyCondition=" + processorNotifyCondition +
            '}';
    }
}
