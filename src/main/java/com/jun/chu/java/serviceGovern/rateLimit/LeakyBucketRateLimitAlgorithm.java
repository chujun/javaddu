package com.jun.chu.java.serviceGovern.rateLimit;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chujun
 * @date 2022/6/24
 */
public class LeakyBucketRateLimitAlgorithm {
    /**
     * 每秒处理数（出水率）
     */
    private long rateConfig = 10;

    /**
     * 桶容量
     */
    private long capacityConfig = 1000;

    /**
     * 当前剩余水量
     */
    private long remainingWater = 0;

    /**
     * 最后刷新时间
     */
    private long refreshTime = 0;

    private ReentrantLock lock = new ReentrantLock();

    public LeakyBucketRateLimitAlgorithm() {
    }

    public LeakyBucketRateLimitAlgorithm(final long rateConfig, final long capacityConfig) {
        this.rateConfig = rateConfig;
        this.capacityConfig = capacityConfig;
    }

    /**
     * 漏桶算法
     *
     * @return
     */
    boolean leakyBucketLimitTryAcquire() {
        lock.lock();
        try {
            //获取系统当前时间
            long currentTime = System.currentTimeMillis();
            //流出的水量 =(当前时间-上次刷新时间)* 出水率
            long outWater = (currentTime - refreshTime) / 1000 * rateConfig;
            // 当前水量 = 之前的桶内水量-流出的水量

            remainingWater = Math.max(0, remainingWater - outWater);

            // 刷新时间
            refreshTime = currentTime;

            // 当前剩余水量还是小于桶的容量，则请求放行
            if (remainingWater < capacityConfig) {
                remainingWater++;
                return true;
            }

            // 当前剩余水量大于等于桶的容量，限流
            return false;
        } finally {
            lock.unlock();
        }
    }
}
