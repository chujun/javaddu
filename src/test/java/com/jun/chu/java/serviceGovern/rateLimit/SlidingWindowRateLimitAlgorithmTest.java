package com.jun.chu.java.serviceGovern.rateLimit;

import org.junit.Test;

/**
 * @author chujun
 * @date 2022/6/23
 */
public class SlidingWindowRateLimitAlgorithmTest {
    @Test
    public void test() throws InterruptedException {
        SlidingWindowRateLimitAlgorithm slidingWindowRateLimitAlgorithm =
            new SlidingWindowRateLimitAlgorithm(10, 10);
        for (int i = 0; i < 1000; i++) {
            System.out.println(slidingWindowRateLimitAlgorithm.slidingWindowsTryAcquire() + "，" + slidingWindowRateLimitAlgorithm.getTotalStoredSize());
            Thread.sleep(100);
        }
    }

    @Test
    public void testV2() throws InterruptedException {
        SlidingWindowRateLimitV2Algorithm slidingWindowRateLimitV2Algorithm =
            new SlidingWindowRateLimitV2Algorithm(5, 5, 10);
        for (int i = 0; i < 100; i++) {
            System.out.println(slidingWindowRateLimitV2Algorithm.slidingWindowsTryAcquire() + "，" + slidingWindowRateLimitV2Algorithm.getTotalStoredSize());
            Thread.sleep(100);
        }
    }

    @Test
    public void testV22() throws InterruptedException {
        SlidingWindowRateLimitV2Algorithm slidingWindowRateLimitV2Algorithm =
            new SlidingWindowRateLimitV2Algorithm(5, 5, 10);
        for (int i = 0; i < 100; i++) {
            System.out.println(slidingWindowRateLimitV2Algorithm.slidingWindowsTryAcquire() + "，" + slidingWindowRateLimitV2Algorithm.getTotalStoredSize());
            Thread.sleep(300);
        }
    }
}
