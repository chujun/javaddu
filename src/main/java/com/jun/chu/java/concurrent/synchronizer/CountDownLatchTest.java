package com.jun.chu.java.concurrent.synchronizer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by chujun on 17/2/14.
 * concurrent并发包的同步器:倒计数锁存器
 * 参考资料:effective java中文版第二版 第10章并发第69条并发工具优先于wait和notify p241
 */
public class CountDownLatchTest {
    private static final int DEFAULT_THREAD_POOL_NUM = Runtime.getRuntime().availableProcessors() * 2;

    public static void main(String[] args) throws InterruptedException {
        Executor executor = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_NUM);
        final long nanoTime = SimpleTimingConcurrentExecutor.time(executor, DEFAULT_THREAD_POOL_NUM / 2, new BaseRunnable() {
            public void run() {
                long sum = 0;
                for (long i = 0; i < 100 * 100 * 100 ; i++) {
                    sum += i;
                }
                echo("" + sum);
            }


        });
        System.out.println(String.format("total cost time:%s ms", nanoTime / 1000 / 1000));
    }
}


class SimpleTimingConcurrentExecutor {
    /**
     * 要求 executor的线程池数必须大于concurrent,否则会出现线程饥饿死锁。
     *
     * @param executor
     * @param concurrent
     * @param action
     * @return
     */
    public static long time(Executor executor, int concurrent, final Runnable action) throws InterruptedException {
        final CountDownLatch ready = new CountDownLatch(concurrent);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(concurrent);

        for (int i = 0; i < concurrent; i++) {
            executor.execute(new BaseRunnable() {
                public void run() {
                    ready.countDown();
                    echo("ready finished");
                    try {
                        start.await();
                        echo("action start run");
                        long startNanoTime = System.nanoTime();
                        action.run();
                        echo(String.format("action finished:cost time:%s ms", (System.nanoTime() - startNanoTime) / 1000 / 1000));
                        done.countDown();
                        echo("run finished");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }

                }
            });
        }

        System.out.println("ready await");
        ready.await();
        System.out.println("ready finished");
        long startNanoTime = System.nanoTime();
        System.out.println("start start");
        start.countDown();
        done.await();
        System.out.println("done finished");
        return System.nanoTime() - startNanoTime;
    }
}

abstract class BaseRunnable implements Runnable {
    public void echo(String str) {
        System.out.println(getThreadName() + ":" + str);
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }
}

/**
 * 运行结果
 * pool-1-thread-1:ready finished
 * pool-1-thread-4:ready finished
 * pool-1-thread-3:ready finished
 * ready await
 * pool-1-thread-2:ready finished
 * ready finished
 * start start
 * pool-1-thread-1:action start run
 * pool-1-thread-4:action start run
 * pool-1-thread-2:action start run
 * pool-1-thread-3:action start run
 * pool-1-thread-3:994142226714070528
 * pool-1-thread-4:994142226714070528
 * pool-1-thread-2:994142226714070528
 * pool-1-thread-1:994142226714070528
 * pool-1-thread-2:action finished:cost time:1550 ms
 * pool-1-thread-2:run finished
 * pool-1-thread-4:action finished:cost time:1548 ms
 * pool-1-thread-4:run finished
 * pool-1-thread-1:action finished:cost time:1551 ms
 * pool-1-thread-1:run finished
 * pool-1-thread-3:action finished:cost time:1532 ms
 * pool-1-thread-3:run finished
 * done finished
 * total cost time:1564 ms
 *
 *  测试报告
 *  mac4核,8个固定线程,concurrent为4
 *  计算总数                      total(ms)
 *  100 * 100 * 100 * 100 * 100    1564   1550,1548,1551,1532
 *  100 * 100 * 100 * 100           126    117,95,99,101
 *  100 * 100 * 100                  48     14,29,18,26
 */
