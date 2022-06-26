package com.jun.chu.java.serviceGovern.rateLimit.guava;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author chujun
 * @date 2022/6/25
 */
public class RateLimiterTest {
    @Test
    public void test() {
        RateLimiter limiter = RateLimiter.create(1);

        for (int i = 1; i < 10; i = i + 2) {
            double waitTime = limiter.acquire(i);
            System.out.println("cutTime=" + System.currentTimeMillis() + " acq:" + i + " waitTime:" + waitTime);
        }
    }

    @Test
    public void testWarmup() {
        // 1s 放 5 个令牌到桶里也就是 0.2s 放 1个令牌到桶里
        // 预热时间为3s,也就说刚开始的 3s 内发牌速率会逐渐提升到 0.2s 放 1 个令牌到桶里
        RateLimiter rateLimiter = RateLimiter.create(5, 3, TimeUnit.SECONDS);
        for (int i = 0; i < 20; i++) {
            double sleepingTime = rateLimiter.acquire(1);
            System.out.printf("get 1 tokens: %sds%n", sleepingTime);
        }
    }

    @Test
    public void test2() throws InterruptedException {
        // qps设置为5，代表一秒钟只允许处理五个并发请求
        RateLimiter rateLimiter = RateLimiter.create(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        int nTasks = 10;
        CountDownLatch countDownLatch = new CountDownLatch(nTasks);
        long start = System.currentTimeMillis();
        for (int i = 0; i < nTasks; i++) {
            final int j = i;
            executorService.submit(() -> {
                rateLimiter.acquire(1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                System.out.println(Thread.currentThread().getName() + " gets job " + j + " done");
                countDownLatch.countDown();
            });
        }
        executorService.shutdown();
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("10 jobs gets done by 5 threads concurrently in " + (end - start) + " milliseconds");

    }
}
