package com.jun.chu.java.serviceGovern.rateLimit;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 固定窗口限流算法
 *
 * @author chujun
 * @date 2022/6/23
 */
public class FixedWindowRateLimitAlgorithm {
    /*默认配置:1s内访问限流阈值10个*/
    private int thresholdConfig = 10;
    private int windowUnitSecondConfig = 1;

    private AtomicInteger counter = new AtomicInteger(0);

    private AtomicLong lastRequestTime = new AtomicLong(0);

    public FixedWindowRateLimitAlgorithm() {
    }

    public FixedWindowRateLimitAlgorithm(final int thresholdConfig, final int windowUnitSecondConfig) {
        this.thresholdConfig = thresholdConfig;
        this.windowUnitSecondConfig = windowUnitSecondConfig;
    }

    /**
     * 固定窗口时间算法
     *
     * @return
     */
    boolean fixedWindowsTryAcquire() {
        //获取系统当前时间
        long currentTime = System.currentTimeMillis();
        //检查是否在时间窗口内
        if (currentTime - lastRequestTime.get() > (windowUnitSecondConfig * 1000)) {
            // 计数器清0
            counter.compareAndSet(counter.get(), 0);
            //开启新的时间窗口
            lastRequestTime.compareAndSet(lastRequestTime.get(), currentTime);
            System.out.println(counter.get() + "," + lastRequestTime.get());
        }
        // 小于阀值
        System.out.println(counter.get() + "," + thresholdConfig);
        if (counter.get() < thresholdConfig) {
            //计数器加1
            counter.getAndIncrement();
            return true;
        }

        return false;
    }
}
