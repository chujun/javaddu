package com.jun.chu.java.serviceGovern.rateLimit;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author chujun
 * @date 2022/6/24
 */
public class TokenBucketRateLimitAlgorithmTest {
    @Test
    public void test0() throws InterruptedException {
        TokenBucketRateLimitAlgorithm tokenBucketRateLimitAlgorithm =
            new TokenBucketRateLimitAlgorithm(10, 20);
        for (int i = 0; i < 100; i++) {
            Thread.sleep(100);
            Assert.assertTrue(tokenBucketRateLimitAlgorithm.tokenBucketTryAcquire());
        }
    }

    @Test
    public void test1() throws InterruptedException {
        TokenBucketRateLimitAlgorithm tokenBucketRateLimitAlgorithm =
            new TokenBucketRateLimitAlgorithm(10, 20);
        for (int i = 0; i < 100; i++) {
            //System.out.println("index:" + i + ",result:" + tokenBucketRateLimitAlgorithm.tokenBucketTryAcquire());
            Thread.sleep(50);
            //50*38=1900ms===>19个令牌，19+20
            if (i < 39) {
                Assert.assertTrue(tokenBucketRateLimitAlgorithm.tokenBucketTryAcquire());
            } else if (i % 2 == 1) {
                Assert.assertFalse(tokenBucketRateLimitAlgorithm.tokenBucketTryAcquire());
            } else {
                Assert.assertTrue(tokenBucketRateLimitAlgorithm.tokenBucketTryAcquire());
            }
        }
    }
}
