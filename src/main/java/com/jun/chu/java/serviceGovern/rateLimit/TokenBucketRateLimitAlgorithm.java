package com.jun.chu.java.serviceGovern.rateLimit;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 令牌桶算法
 *
 * @author chujun
 * @date 2022/6/24
 */
public class TokenBucketRateLimitAlgorithm {
    /**
     * 每秒处理数（放入令牌数量）
     */
    private long putTokenPerSecondRateConfig = 10;

    /**
     * 令牌桶容量
     */
    private long capacityConfig = 100;

    /**
     * 最后刷新时间
     */
    private long refreshTime;

    /**
     * 当前桶内令牌数
     */
    private long currentToken = 0L;

    private ReentrantLock lock = new ReentrantLock();

    public TokenBucketRateLimitAlgorithm() {
    }

    public TokenBucketRateLimitAlgorithm(final long putTokenPerSecondRateConfig, final long capacityConfig) {
        this.putTokenPerSecondRateConfig = putTokenPerSecondRateConfig;
        this.capacityConfig = capacityConfig;
    }

    /**
     * 漏桶算法
     */
    public boolean tokenBucketTryAcquire() {
        lock.lock();
        try {
            //获取系统当前时间
            long currentTime = System.currentTimeMillis();
            //生成的令牌 =(当前时间-上次刷新时间)* 放入令牌的速率，生成token数也不是特别进准,小数部分被舍弃了
            long generateToken = (long) ((currentTime - refreshTime) / 1000.0 * putTokenPerSecondRateConfig);
            // 当前令牌数量 = 之前的桶内令牌数量+放入的令牌数量
            currentToken = Math.min(capacityConfig, generateToken + currentToken);
            // 刷新时间
            if (generateToken > 0) {
                refreshTime = currentTime;
            }

            //桶里面还有令牌，请求正常处理
            if (currentToken > 0) {
                //令牌数量-1
                currentToken--;
                return true;
            }

            return false;
        } finally {
            lock.unlock();
        }
    }
}
