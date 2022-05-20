package com.jun.chu.java.leetcode.search;

import org.junit.Assert;
import org.junit.Test;

/**
 * [704. leetcode 二分查找](https://leetcode.cn/problems/binary-search/)
 * 给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target
 * 没有重复元素
 *
 * @author chujun
 * @date 2022/5/20
 */
public class BinarySearch {
    private static int NOT_FIND = -1;

    @Test
    public void test() {
        Assert.assertEquals(0, search(new int[]{-5}, -5));
        Assert.assertEquals(1, search(new int[]{-10, -5, 0, 1, 2}, -5));
        Assert.assertEquals(-1, search(new int[]{-10, -5, 0, 1, 2}, 3));
        Assert.assertEquals(0, search(new int[]{-5, 0, 1, 2}, -5));
        Assert.assertEquals(-1, search(new int[]{-5, 0, 1, 2}, -4));
    }

    @Test
    public void test2() {
        Assert.assertEquals(0, search2(new int[]{-5}, -5));
        Assert.assertEquals(1, search2(new int[]{-10, -5, 0, 1, 2}, -5));
        Assert.assertEquals(-1, search2(new int[]{-10, -5, 0, 1, 2}, 3));
        Assert.assertEquals(0, search2(new int[]{-5, 0, 1, 2}, -5));
        Assert.assertEquals(-1, search2(new int[]{-5, 0, 1, 2}, -4));

    }

    /**
     * 写法一:明确区间定义[left,right],左闭右闭
     * 循环不变量
     */
    public int search(int[] nums, int target) {
        if (null == nums || 0 == nums.length) {
            return NOT_FIND;
        }
        // 定义target在左闭右闭的区间里，[left, right]
        int left = 0, right = nums.length - 1, middle = (left + right) >> 1;
        // 当left==right，区间[left, right]依然有效，所以用 <=
        for (; left <= right; middle = (left + right) >> 1) {
            //左闭右闭区间，明确left,right相等情况下也可产生满足条件的结果
            if (nums[middle] == target) {
                return middle;
            } else if (nums[middle] > target) {
                // target 在左区间，所以[left, middle - 1]
                right = middle - 1;
            } else {
                // target 在右区间，所以[middle + 1, right]
                left = middle + 1;
            }
        }
        return NOT_FIND;
    }

    /**
     * 写法二:明确区间定义[left,right),左闭右开,意味着right处下标数组不是结果
     * 循环不变量
     */
    public int search2(int[] nums, int target) {
        if (null == nums || 0 == nums.length) {
            return NOT_FIND;
        }
        // 定义target在左闭右开的区间里，即：[left, right) 与写法一不同之处一
        int left = 0, right = nums.length, middle = (left + right) >> 1;
        //左闭右开区间，明确left,right相等情况下则应该跳出循环了
        //因为left == right的时候，在[left, right)是无效的空间，所以使用 <，与写法一不同之处二
        for (; left < right; middle = (left + right) >> 1) {
            if (nums[middle] == target) {
                return middle;
            } else if (nums[middle] > target) {
                // target 在左区间，在[left, middle)中,与写法一不同之处三
                right = middle;
            } else {
                // target 在右区间，在[middle + 1, right)中
                left = middle + 1;
            }
        }
        return NOT_FIND;
    }
}
