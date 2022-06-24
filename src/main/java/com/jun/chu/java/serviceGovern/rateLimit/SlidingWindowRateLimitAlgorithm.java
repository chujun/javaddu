package com.jun.chu.java.serviceGovern.rateLimit;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 滑动窗口限流算法
 *
 * @author chujun
 * @date 2022/6/23
 */
public class SlidingWindowRateLimitAlgorithm {
    /**
     * 单位时间划分的小周期（单位时间是1分钟，10s一个小格子窗口，一共6个格子）
     */
    private int subCyclePerMinConfig = 10;

    /**
     * 每分钟限流请求数
     */
    private int thresholdPerMinConfig = 100;

    /**
     * 计数器, k-为当前窗口的开始时间值秒，value为当前窗口的计数
     */
    private TreeMap<Long, Integer> counters = new TreeMap<>();

    public SlidingWindowRateLimitAlgorithm() {
    }

    public SlidingWindowRateLimitAlgorithm(final int subCyclePerMinConfig, final int thresholdPerMinConfig) {
        this.subCyclePerMinConfig = subCyclePerMinConfig;
        this.thresholdPerMinConfig = thresholdPerMinConfig;
    }

    /**
     * 滑动窗口时间算法实现
     */
    public boolean slidingWindowsTryAcquire() {
        //获取当前时间在哪个小周期窗口:小周期窗口开始时间点
        long currentSubCycleWindowStartTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) / subCyclePerMinConfig * subCyclePerMinConfig;
        //当前窗口总请求数
        int currentWindowNum = countCurrentWindow(currentSubCycleWindowStartTime);

        //超过阀值限流
        if (currentWindowNum >= thresholdPerMinConfig) {
            return false;
        }

        //计数器+1
        counters.merge(currentSubCycleWindowStartTime, 1, Integer::sum);
        return true;
    }

    /**
     * 统计当前窗口的请求数
     */
    private int countCurrentWindow(long currentSubCycleWindowStartTime) {
        //计算窗口开始位置
        long startTime = currentSubCycleWindowStartTime - subCyclePerMinConfig * (60 / subCyclePerMinConfig - 1);
        int count = 0;

        //遍历存储的计数器
        Iterator<Map.Entry<Long, Integer>> iterator = counters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> entry = iterator.next();
            // 删除无效过期的子窗口计数器，不删除的话数据会一直堆积
            if (entry.getKey() < startTime) {
                iterator.remove();
            } else {
                //累加当前窗口的所有计数器之和
                count = count + entry.getValue();
            }
        }
        return count;
    }

    public int getTotalStoredSize() {
        return counters.size();
    }
}
