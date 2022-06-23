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
}
