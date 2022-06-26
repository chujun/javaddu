package com.jun.chu.java.mulithread.alternateprint;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 要求使用三个线程,交替打印出"abcabcabcabc"100次
 *
 * @author chujun
 * @date 2022/6/26
 */
public class AlternatePrintThreeNumber {
    private Lock lock = new ReentrantLock();
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();
    private CountDownLatch threadAStart = new CountDownLatch(1);
    private CountDownLatch end = new CountDownLatch(3);

    public void alternatePrint() throws InterruptedException {
        Thread a = new Thread(() -> {
            boolean first = true;
            for (int i = 0; i < 100; i++) {
                System.out.println("a");
                if (first) {
                    threadAStart.countDown();
                    first = false;
                }
                lock.lock();
                try {
                    conditionB.signalAll();
                    conditionA.await();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            end.countDown();
            lock.lock();
            try {
                conditionB.signalAll();
                conditionC.signalAll();
            } finally {
                lock.unlock();
            }
        });
        Thread b = new Thread(() -> {
            try {
                threadAStart.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 100; i++) {
                System.out.println("b");
                lock.lock();
                try {
                    conditionC.signalAll();
                    conditionB.await();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            end.countDown();
        });
        Thread c = new Thread(() -> {
            try {
                threadAStart.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 100; i++) {
                System.out.println("c");
                lock.lock();
                try {
                    conditionA.signalAll();
                    conditionC.await();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            end.countDown();
        });
        a.start();
        b.start();
        c.start();
        end.await();
        System.out.println("print 100 abc finished");
    }

    @Test
    public void test() throws InterruptedException {
        AlternatePrintThreeNumber alternatePrintThreeNumber = new AlternatePrintThreeNumber();
        alternatePrintThreeNumber.alternatePrint();
    }
}
