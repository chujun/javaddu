package com.jun.chu.java.serviceGovern.rateLimit;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author chujun
 * @date 2022/6/24
 */
public class LeakyBucketRateLimitAlgorithmTest {
    @Test
    public void test0() throws InterruptedException {
        LeakyBucketRateLimitAlgorithm leakyBucketRateLimitAlgorithm =
            new LeakyBucketRateLimitAlgorithm(10, 20);
        for (int i = 0; i < 50; i++) {
            Thread.sleep(100);
            Assert.assertTrue(leakyBucketRateLimitAlgorithm.leakyBucketLimitTryAcquire());
        }
    }

    @Test
    public void test2() throws InterruptedException {
        LeakyBucketRateLimitAlgorithm leakyBucketRateLimitAlgorithm =
            new LeakyBucketRateLimitAlgorithm(10, 20);
        for (int i = 0; i < 100; i++) {
            Thread.sleep(90);
            if (i < 20) {
                Assert.assertTrue(leakyBucketRateLimitAlgorithm.leakyBucketLimitTryAcquire());
            } else {
                System.out.println("result,index:" + i + "," + leakyBucketRateLimitAlgorithm.leakyBucketLimitTryAcquire());
            }

        }
    }

    @Test
    public void test3() throws InterruptedException {
        LeakyBucketRateLimitAlgorithm leakyBucketRateLimitAlgorithm =
            new LeakyBucketRateLimitAlgorithm(10, 20);
        for (int i = 0; i < 100; i++) {
            System.out.println("result,index:" + i + "," + leakyBucketRateLimitAlgorithm.leakyBucketLimitTryAcquire());
            Thread.sleep(50);
            if (i % 30 == 0) {
                Thread.sleep(500);
            }
        }
    }
}
