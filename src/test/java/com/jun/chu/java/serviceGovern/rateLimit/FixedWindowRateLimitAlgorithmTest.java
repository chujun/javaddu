package com.jun.chu.java.serviceGovern.rateLimit;

import org.junit.Test;

/**
 * @author chujun
 * @date 2022/6/23
 */
public class FixedWindowRateLimitAlgorithmTest {
    @Test
    public void test() throws InterruptedException {
        FixedWindowRateLimitAlgorithm fixedWindowRateLimitAlgorithm = new FixedWindowRateLimitAlgorithm(10, 1);
        for (int i = 0; i < 100; i++) {
            System.out.println(System.currentTimeMillis() + ":能否访问:"
                + fixedWindowRateLimitAlgorithm.fixedWindowsTryAcquire());
            if (i % 20 == 0) {
                Thread.sleep(500);
            }
        }
    }
}
