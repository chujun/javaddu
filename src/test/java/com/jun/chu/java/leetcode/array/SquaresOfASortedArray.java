package com.jun.chu.java.leetcode.array;

import com.jun.chu.java.leetcode.tools.OriginJDKUtil;
import org.junit.Test;

/**
 * [997.排序数组平方](https://leetcode.cn/problems/squares-of-a-sorted-array/)
 * 时间复杂度On
 *
 * @author chujun
 * @date 2022/6/26
 */
public class SquaresOfASortedArray {
    /**
     * 分情况谈论:
     * 1.不存在负数，数组顺序不变
     * 2.存在负数,平方和顺序：大--->小--0->小--->大
     * 双指针法,
     * 思路:一个指针指向数组最左边，另一个指针指向数组最右边,每次取最大值获取最大值放到数组右边
     * 时间复杂度:O(n)
     * 空间复杂度:O(n)
     */
    public int[] sortedSquares(int[] nums) {
        if (null == nums || 0 == nums.length) {
            return nums;
        }
        int leftIndex = 0;
        int rightIndex = nums.length - 1;
        int[] result = new int[nums.length];
        int index = nums.length - 1;
        while (rightIndex >= leftIndex) {
            int leftSquaresValue = nums[leftIndex] * nums[leftIndex];
            int rightSquaresValue = nums[rightIndex] * nums[rightIndex];
            if (leftSquaresValue > rightSquaresValue) {
                result[index--] = leftSquaresValue;
                leftIndex++;
            } else {
                result[index--] = rightSquaresValue;
                rightIndex--;
            }

        }
        return result;
    }

    @Test
    public void test() {
        int[] nums = new int[]{-4, -3, -1, 0, 2, 3, 4, 5};
        SquaresOfASortedArray squaresOfASortedArray = new SquaresOfASortedArray();
        System.out.println(OriginJDKUtil.newCopyList(squaresOfASortedArray.sortedSquares(nums)));
    }

    @Test
    public void test2() {
        int[] nums = new int[]{0, 2, 3, 4, 5};
        SquaresOfASortedArray squaresOfASortedArray = new SquaresOfASortedArray();
        System.out.println(OriginJDKUtil.newCopyList(squaresOfASortedArray.sortedSquares(nums)));
    }
}
