package com.jun.chu.java.leetcode.permutation;

import com.jun.chu.java.leetcode.tools.OriginJDKUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 针对含有重复数字组合的全排列，按任意顺序 返回所有不重复的全排列
 * [leetcode47 全排列II](https://leetcode.cn/problems/permutations-ii/)
 *
 * @author chujun
 * @date 2022/5/19
 */
public class AllPermutationForMaySampleNumber {
    @Test
    public void testNotHaveSimple() {
        int[] a = new int[10];
        System.out.println(a.length);
        System.out.println(a[0] + "," + a[1]);
        List<Integer> visitedList = new ArrayList<>();
        System.out.println(visitedList.size());
        visitedList.add(1);
        System.out.println(visitedList.size());
        //不存在相同数字
        Assert.assertEquals(permuteUnique(new int[]{1}).toString(), "[[1]]");
        Assert.assertEquals(permuteUnique(new int[]{1, 2}).toString(), "[[1, 2], [2, 1]]");
        Assert.assertEquals(permuteUnique(new int[]{1, 2, 3}).toString(), "[[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 2, 1], [3, 1, 2]]");
        Assert.assertEquals(permuteUnique(new int[]{1, 2, 3, 4}).toString(), "[[1, 2, 3, 4], [1, 2, 4, 3], [1, 3, 2, 4], [1, 3, 4, 2], [1, 4, 3, 2], [1, 4, 2, 3], [2, 1, 3, 4], [2, 1, 4, 3], [2, 3, 1, 4], [2, 3, 4, 1], [2, 4, 3, 1], [2, 4, 1, 3], [3, 2, 1, 4], [3, 2, 4, 1], [3, 1, 2, 4], [3, 1, 4, 2], [3, 4, 1, 2], [3, 4, 2, 1], [4, 2, 3, 1], [4, 2, 1, 3], [4, 3, 2, 1], [4, 3, 1, 2], [4, 1, 3, 2], [4, 1, 2, 3]]");

    }

    @Test
    public void testHaveSimple() {
        //存在相同数字
        Assert.assertEquals(permuteUnique(new int[]{1, 1}).toString(), "[[1, 1]]");
        Assert.assertEquals(permuteUnique(new int[]{1, 1, 2}).toString(), "[[1, 1, 2], [1, 2, 1], [2, 1, 1]]");
        Assert.assertEquals(permuteUnique(new int[]{1, 1, 2, 3}).toString(), "[[1, 1, 2, 3], [1, 1, 3, 2], [1, 2, 1, 3], [1, 2, 3, 1], [1, 3, 2, 1], [1, 3, 1, 2], [2, 1, 1, 3], [2, 1, 3, 1], [2, 3, 1, 1], [3, 1, 2, 1], [3, 1, 1, 2], [3, 2, 1, 1]]");
        Assert.assertEquals(permuteUnique(new int[]{2, 2, 1, 1}).toString(), "[[2, 2, 1, 1], [2, 1, 2, 1], [2, 1, 1, 2], [1, 2, 2, 1], [1, 2, 1, 2], [1, 1, 2, 2]]");
        Assert.assertEquals(permuteUnique(new int[]{10000, 1, 10000, 1}).toString(), "[[10000, 1, 10000, 1], [10000, 1, 1, 10000], [10000, 10000, 1, 1], [1, 10000, 10000, 1], [1, 10000, 1, 10000], [1, 1, 10000, 10000]]");
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        if (null == nums || 0 == nums.length) {
            return Collections.emptyList();
        }
        List<List<Integer>> result = new ArrayList<>();
        permuteUnique(result, 0, nums);
        return result;
    }

    /**
     * 画图可知一个特殊规律,
     * 当采用全排列算法时交换数字时,当交换的数字相同时,则新生成的子树必然相同
     */
    private void permuteUnique(List<List<Integer>> result,
                               int traverseLocation,
                               int[] nums) {
        //有限解
        if (traverseLocation == nums.length - 1) {
            List<Integer> oneResult = OriginJDKUtil.newCopyList(nums);
            result.add(oneResult);
        }
        //递归回溯解(横向遍历)
        Set<Integer> visitedSet = new HashSet<>();
        for (int i = traverseLocation; i < nums.length; i++) {
            //swap交换位置数值
            if (i != traverseLocation) {
                //规律一:这儿是去重的关键:通过规律寻找,如果算法发现需要交换位置的数值相同时，则接下来纵向递归遍历生成的子树必然相同
                if (nums[traverseLocation] == nums[i]) {
                    continue;
                }
                //规律二:同一层和相同的数字交换过,那么生成的子树也必然相同,例如[2,2,1,1],2会和后面的1,1交换,
                //所以[2,1|2,1]和[2,1|1,2]两个接下来生成的子树必然也相同
                if (visitedSet.contains(nums[i])) {
                    continue;
                } else {
                    visitedSet.add(nums[i]);
                }
                swap(nums, traverseLocation, i);
            }
            //纵向递归遍历
            permuteUnique(result, traverseLocation + 1, nums);
            //revert状态重置回来,交换回来位置数值
            swap(nums, traverseLocation, i);
        }
    }

    private void swap(final int[] nums, final int traverseLocation, final int i) {
        int a = nums[traverseLocation];
        nums[traverseLocation] = nums[i];
        nums[i] = a;
    }
}
