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
public class SlidingWindowRateLimitV2Algorithm {
    /**
     * 单位划分的小周期（默认单位时间是1分钟，10s一个小格子窗口，一共6个格子）
     */
    private int subCyclePerWindowConfig = 10;

    /**
     * 窗口大小,单位:秒
     */
    private int windowSecondConfig = 60;

    /**
     * 请求限流阈值
     */
    private int thresholdConfig = 100;

    /**
     * 计数器, k-为当前窗口的开始时间值秒，value为当前窗口的计数
     */
    private TreeMap<Long, Integer> counters = new TreeMap<>();

    public SlidingWindowRateLimitV2Algorithm() {
    }

    public SlidingWindowRateLimitV2Algorithm(final int subCyclePerWindowConfig,
                                             final int windowSecondConfig,
                                             final int thresholdConfig) {
        this.subCyclePerWindowConfig = subCyclePerWindowConfig;
        this.windowSecondConfig = windowSecondConfig;
        this.thresholdConfig = thresholdConfig;
    }

    /**
     * 滑动窗口时间算法实现
     */
    boolean slidingWindowsTryAcquire() {
        //获取当前时间在哪个小周期窗口:小周期窗口开始时间点
        long currentSubCycleWindowStartTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) / subCyclePerWindowConfig * subCyclePerWindowConfig;
        //当前窗口总请求数
        int currentWindowNum = countCurrentWindow(currentSubCycleWindowStartTime);

        //超过阀值限流
        if (currentWindowNum >= thresholdConfig) {
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
        long startTime = currentSubCycleWindowStartTime - subCyclePerWindowConfig * (windowSecondConfig / subCyclePerWindowConfig - 1);
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
                count += entry.getValue();
            }
        }
        return count;
    }

    public int getTotalStoredSize() {
        return counters.size();
    }
}
