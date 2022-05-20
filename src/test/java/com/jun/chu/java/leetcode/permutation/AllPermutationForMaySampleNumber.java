package com.jun.chu.java.leetcode.permutation;

import com.jun.chu.java.leetcode.tools.OriginJDKUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Test
    public void testNotHaveSimpleV100() {
        //不存在相同数字
        Assert.assertEquals(permuteUniqueQuestion(new int[]{1}).toString(), "[[1]]");
        Assert.assertEquals(permuteUniqueQuestion(new int[]{1, 2}).toString(), "[[1, 2], [2, 1]]");
        Assert.assertEquals(permuteUniqueQuestion(new int[]{1, 2, 3}).toString(), "[[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 2, 1], [3, 1, 2]]");
        Assert.assertEquals(permuteUniqueQuestion(new int[]{1, 2, 3, 4}).toString(), "[[1, 2, 3, 4], [1, 2, 4, 3], [1, 3, 2, 4], [1, 3, 4, 2], [1, 4, 3, 2], [1, 4, 2, 3], [2, 1, 3, 4], [2, 1, 4, 3], [2, 3, 1, 4], [2, 3, 4, 1], [2, 4, 3, 1], [2, 4, 1, 3], [3, 2, 1, 4], [3, 2, 4, 1], [3, 1, 2, 4], [3, 1, 4, 2], [3, 4, 1, 2], [3, 4, 2, 1], [4, 2, 3, 1], [4, 2, 1, 3], [4, 3, 2, 1], [4, 3, 1, 2], [4, 1, 3, 2], [4, 1, 2, 3]]");

    }

    @Test
    public void testHaveSimpleV100() {
        //存在相同数字
        Assert.assertEquals(permuteUniqueQuestion(new int[]{1, 1}).toString(), "[[1, 1]]");
        Assert.assertEquals(permuteUniqueQuestion(new int[]{1, 1, 2}).toString(), "[[1, 1, 2], [1, 2, 1], [2, 1, 1]]");
        Assert.assertEquals(permuteUniqueQuestion(new int[]{1, 1, 2, 3}).toString(), "[[1, 1, 2, 3], [1, 1, 3, 2], [1, 2, 1, 3], [1, 2, 3, 1], [1, 3, 2, 1], [1, 3, 1, 2], [2, 1, 1, 3], [2, 1, 3, 1], [2, 3, 1, 1], [3, 1, 2, 1], [3, 1, 1, 2], [3, 2, 1, 1]]");
        Assert.assertEquals(permuteUniqueQuestion(new int[]{2, 2, 1, 1}).toString(), "[[2, 2, 1, 1], [2, 1, 2, 1], [2, 1, 1, 2], [1, 2, 2, 1], [1, 2, 1, 2], [1, 1, 2, 2]]");
        Assert.assertEquals(permuteUniqueQuestion(new int[]{10000, 1, 10000, 1}).toString(), "[[10000, 1, 10000, 1], [10000, 1, 1, 10000], [10000, 10000, 1, 1], [1, 10000, 10000, 1], [1, 10000, 1, 10000], [1, 1, 10000, 10000]]");
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
     * 数组traverseLocation位置交换数字时，如果这个数值之前访问过，则不能交换，因为剩下的数组元素必然相同
     * 当采用全排列算法时交换数字时,当交换的数字相同时,则新生成的子树必然相同
     */
    private void permuteUnique(List<List<Integer>> result,
                               int traverseLocation,
                               int[] nums) {
        //有限解
        if (traverseLocation == nums.length - 1) {
            List<Integer> oneResult = OriginJDKUtil.newCopyList(nums);
            result.add(oneResult);
            return;
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

    /**
     * 这个算法存在问题
     * 相比较permuteUnique版本，
     * 先对原始数组排序(时间复杂度On)，这样的好处是同一层横向遍历时后续判断是否访问过数值,
     * 可以无需要visitedSet,直接和前一个访问过的数值相比较即可
     * 时间复杂度On并没有增加，还是On=O(n!*n)
     * <p>
     * 因为数组后面swap交换过，所以数组并不是排序好的，例如[1|,1,2,3]->[3|,1,2,1],traverseLocation为0的1和最后的3交换位置
     * ,当前数值需要交换的数值和上一次访问过的数值相同的话，则跳过
     */
    public List<List<Integer>> permuteUniqueQuestion(int[] nums) {
        if (null == nums || 0 == nums.length) {
            return Collections.emptyList();
        }
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        permuteUniqueQuestion(result, 0, nums);
        return result;
    }

    /**
     * 画图可知一个特殊规律,
     * 数组traverseLocation位置交换数字时，如果这个数值之前访问过，则不能交换，因为剩下的数组元素必然相同
     * 这儿有问题:
     */
    private void permuteUniqueQuestion(List<List<Integer>> result,
                                       int traverseLocation,
                                       int[] nums) {
        if (traverseLocation == nums.length) {
            return;
        }
        //有限解
        if (traverseLocation == nums.length - 1) {
            List<Integer> oneResult = OriginJDKUtil.newCopyList(nums);
            result.add(oneResult);
            return;
        }
        //递归回溯解(横向遍历)
        int prevVisited = nums[traverseLocation];
        for (int i = traverseLocation; i < nums.length; i++) {
            //swap交换位置数值
            if (i != traverseLocation) {
                //因为数组后面swap交换过，所以数组并不是排序好的，例如[1|,1,2,3]->[3|,1,2,1],traverseLocation为0的1和最后的3交换位置
                //这儿存在问题
                //当前数值需要交换的数值和上一次访问过的数值相同的话，则跳过
                if (prevVisited == nums[i]) {
                    continue;
                } else {
                    prevVisited = nums[i];
                }
                swap(nums, traverseLocation, i);
            }
            //纵向递归遍历
            permuteUniqueQuestion(result, traverseLocation + 1, nums);
            //revert状态重置回来,交换回来位置数值
            swap(nums, traverseLocation, i);
        }
    }
}
