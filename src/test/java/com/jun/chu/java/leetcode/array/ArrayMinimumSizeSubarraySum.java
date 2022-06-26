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
 * <p>
 * minSubArrayLenV1 和 minSubArrayLenV2 相差不大，时间复杂度都是O(n),各有千秋
 * minSubArrayLenV1:
 * 优点:滑动窗口确认后只会缩小/平移,不会扩大
 * <p>
 * minSubArrayLenV2:
 * 优点:代码简洁,便于理解
 * 滑动窗口还会扩大,
 *
 * @author chujun
 * @date 2022/6/26
 */
public class ArrayMinimumSizeSubarraySum {
    /**
     * 自己思考的方式
     * 算法思路:双指针法，标识滑动窗口，分别指向窗口开始下标和窗口结束下标
     * 窗口就是 满足其和 ≥ s 的长度最小的 连续 子数组。
     * 确认完第一个最大滑动窗口后,滑动窗口只会缩小/平移,不会再扩大了
     * 窗口的起始位置如何移动：如果当前窗口的值大于s了，窗口就要向前移动了（也就是该缩小了）。
     * 时间复杂度：O(n) 空间复杂度：O(1)
     * <p>
     * windowEndIndex递增n次,而windowStartIndex最多递增n次,最多2n次数,所以最坏时间复杂度O(n);
     */
    public int minSubArrayLenV1(int target, int[] nums) {
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
                //缩小滑动窗口
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

    /**
     * 相比minSubArrayLen方法代码比较
     * 窗口就是 满足其和 ≥ s 的长度最小的 连续 子数组。
     * <p>
     * 窗口的起始位置如何移动：如果当前窗口的值大于s了，窗口就要向前移动了（也就是该缩小了）。
     * 窗口的结束位置如何移动：窗口的结束位置就是遍历数组的指针，窗口的起始位置设置为数组的起始位置就可以了。
     * <p>
     * 时间复杂度：O(n) 空间复杂度：O(1)
     * 关于时间复杂度为O(n)的分析:不要以为for里放一个while就以为是O(n^2)啊，
     * windowEndIndex递增n次,而windowStartIndex最多递增n次,最多2n次数,所以最坏时间复杂度O(n);
     */
    public int minSubArrayLenV2(int target, int[] nums) {
        if (null == nums || 0 == nums.length) {
            return 0;
        }
        int result = Integer.MAX_VALUE;
        //滑动窗口开始下标
        int windowStartIndex = 0;
        //滑动窗口结束下标
        int windowEndIndex = 0;
        int sum = 0;
        for (; windowEndIndex < nums.length; windowEndIndex++) {
            //确认完第一个最大滑动窗口后,滑动窗口只会缩小/平移,这儿窗口还会进行扩大
            sum += nums[windowEndIndex];
            while (sum >= target) {
                result = Math.min(result, windowEndIndex - windowStartIndex + 1);
                sum -= nums[windowStartIndex++];
            }
        }
        return result == Integer.MAX_VALUE ? 0 : result;
    }

    @Test
    public void testNotMatchCondition() {
        int[] nums = new int[]{1, 2, 3, 5, 6};
        ArrayMinimumSizeSubarraySum arrayMinimumSizeSubarraySum = new ArrayMinimumSizeSubarraySum();
        Assert.assertEquals(0, arrayMinimumSizeSubarraySum.minSubArrayLenV2(100, nums));
    }

    @Test
    public void testNormal() {
        int[] nums = new int[]{1, 2, 3, 5, 6, 10, 5, 7, 8, 19, 1};
        ArrayMinimumSizeSubarraySum arrayMinimumSizeSubarraySum = new ArrayMinimumSizeSubarraySum();
        System.out.println(arrayMinimumSizeSubarraySum.minSubArrayLenV2(24, nums));
    }
}
