package com.jun.chu.java.mulithread;

import org.junit.Test;

/**
 * @author chujun
 * @date 2022/6/5
 */
public class VolatileTest {
    private static volatile int count = 0;
    private static final int THREAD_COUNT = 20;

    private static void increase() {
        count++;
    }

    @Test
    public void test() throws InterruptedException {
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    increase();
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
        //每次输出结果都不一样，小于10000*20=200000
        System.out.println(count);
    }
}
