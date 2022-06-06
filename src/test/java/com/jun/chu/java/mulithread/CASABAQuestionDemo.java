package com.jun.chu.java.mulithread;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS机制的ABA问题
 * @author chujun
 * @date 2022/6/6
 */
public class CASABAQuestionDemo {
    final AtomicInteger atomicInteger = new AtomicInteger(1);

    @Test
    public void test() throws InterruptedException {
        defectOfABA();
    }

    private void defectOfABA() throws InterruptedException {
        Thread coreThread = new Thread(
            () -> {
                final int currentValue = atomicInteger.get();
                System.out.println(Thread.currentThread().getName() + " ------ currentValue=" + currentValue);

                // 这段目的：模拟处理其他业务花费的时间
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                boolean casResult = atomicInteger.compareAndSet(1, 2);
                System.out.println(Thread.currentThread().getName()
                    + " ------ currentValue=" + currentValue
                    + ", finalValue=" + atomicInteger.get()
                    + ", compareAndSet Result=" + casResult);
            }
        );
        coreThread.start();

        // 这段目的：为了让 coreThread 线程先跑起来
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread amateurThread = new Thread(
            () -> {
                int currentValue = atomicInteger.get();
                boolean casResult = atomicInteger.compareAndSet(1, 2);
                System.out.println(Thread.currentThread().getName()
                    + " ------ currentValue=" + currentValue
                    + ", finalValue=" + atomicInteger.get()
                    + ", compareAndSet Result=" + casResult);

                currentValue = atomicInteger.get();
                casResult = atomicInteger.compareAndSet(2, 1);
                System.out.println(Thread.currentThread().getName()
                    + " ------ currentValue=" + currentValue
                    + ", finalValue=" + atomicInteger.get()
                    + ", compareAndSet Result=" + casResult);
            }
        );
        amateurThread.start();
        amateurThread.join();
        coreThread.join();
    }
}
