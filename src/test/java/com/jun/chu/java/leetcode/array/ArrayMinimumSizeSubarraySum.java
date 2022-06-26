package com.jun.chu.java.leetcode.array;

import org.junit.Assert;
import org.junit.Test;

/**
 * [209. 长度最小的子数组](https://leetcode.cn/problems/minimum-size-subarray-sum/)
 * 给定一个含有n个正整数的数组和一个正整数 target 。
 * <p>
 * 找出该数组中满足其和 ≥ target 的长度最小的 连续子数组[numsl, numsl+1, ..., numsr-1, numsr] ，并返回其长度。如果不存在符合条件的子数组，返回 0 。
 * 示例1
 * 输入：target = 7, nums = [2,3,1,2,4,3]
 * 输出：2
 * 解释：子数组 [4,3] 是该条件下的长度最小的子数组。
 * <p>
 * 示例2
 * 输入：target = 4, nums = [1,4,4]
 * 输出：1
 *
 * @author chujun
 * @date 2022/6/26
 */
public class ArrayMinimumSizeSubarraySum {
    public int minSubArrayLen(int target, int[] nums) {
        if (null == nums || 0 == nums.length) {
            return 0;
        }
        int result = 0;
        //滑动窗口开始下标
        int windowStartIndex = 0;
        //滑动窗口结束下标
        int windowEndIndex = 0;
        int sum = 0;
        boolean sumFlag = true;
        while (windowEndIndex < nums.length) {
            if (sumFlag) {
                sum += nums[windowEndIndex];
            }
            if (sum >= target) {
                //计算当前滑动窗口大小
                result = windowEndIndex - windowStartIndex + 1;
                //缩小滑动窗口一个格子
                sum -= nums[windowStartIndex];
                windowStartIndex++;
                sumFlag = false;
            } else {
                //滑动窗口存在
                if (result > 0) {
                    //滑动窗口向右平移
                    sum -= nums[windowStartIndex];
                    windowStartIndex++;
                } else {
                    //扩大滑动窗口
                }
                sumFlag = true;
                windowEndIndex++;
            }
            if (1 == result) {
                //最小窗口
                break;
            }
        }
        return result;
    }

    @Test
    public void testNotMatchCondition() {
        int[] nums = new int[]{1, 2, 3, 5, 6};
        ArrayMinimumSizeSubarraySum arrayMinimumSizeSubarraySum = new ArrayMinimumSizeSubarraySum();
        Assert.assertEquals(0, arrayMinimumSizeSubarraySum.minSubArrayLen(100, nums));
    }

    @Test
    public void testNormal() {
        int[] nums = new int[]{1, 2, 3, 5, 6, 10, 5, 7, 8, 19, 1};
        ArrayMinimumSizeSubarraySum arrayMinimumSizeSubarraySum = new ArrayMinimumSizeSubarraySum();
        System.out.println(arrayMinimumSizeSubarraySum.minSubArrayLen(24, nums));
    }
}
