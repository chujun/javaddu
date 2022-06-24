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
    private long capacityConfig = 100;

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
            System.out.println("currentTime-refreshTime:" + (currentTime - refreshTime));
            //注意这儿还是存在少量无法组成一个水滴,小数被舍弃掉了，不是特别精准
            long outWater = (long) ((currentTime - refreshTime) / 1000.0 * rateConfig);
            // 当前水量 = 之前的桶内水量-流出的水量

            remainingWater = Math.max(0, remainingWater - outWater);

            // 刷新时间
            if (outWater > 0) {
                refreshTime = currentTime;
            }

            // 当前剩余水量还是小于桶的容量，则请求放行
            if (remainingWater < capacityConfig) {
                System.out.println("++:remainingWater:" + remainingWater + ",currentTime:" + currentTime + ",outWater:" + outWater);
                remainingWater++;
                return true;
            }

            // 当前剩余水量大于等于桶的容量，限流
            System.out.println("remainingWater:" + remainingWater + ",currentTime:" + currentTime + ",outWater:" + outWater);
            return false;
        } finally {
            lock.unlock();
        }
    }
}
