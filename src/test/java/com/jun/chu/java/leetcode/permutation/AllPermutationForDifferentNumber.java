package com.jun.chu.java.leetcode.permutation;

import com.jun.chu.java.leetcode.tools.OriginJDKUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 针对不重复数字组合的全排列
 * [leetcode46 全排列](https://leetcode.cn/problems/permutations/)
 *
 * @author chujun
 * @date 2022/5/19
 */
public class AllPermutationForDifferentNumber {
    @Test
    public void test() {
        System.out.println(permute(new int[]{1}));
        System.out.println(permute(new int[]{1, 2}));
        System.out.println(permute(new int[]{1, 2, 3}));
        List<List<Integer>> result = permute(new int[]{1, 2, 3, 4});
        System.out.println(result);
        System.out.println(result.size());
        Assert.assertEquals(permute(new int[]{1}).toString(), "[[1]]");
        Assert.assertEquals(permute(new int[]{1, 2}).toString(), "[[1, 2], [2, 1]]");
        Assert.assertEquals(permute(new int[]{1, 2, 3}).toString(), "[[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]");
        Assert.assertEquals(permute(new int[]{1, 2, 3, 4}).toString(), "[[1, 2, 3, 4], [1, 2, 4, 3], [1, 3, 2, 4], [1, 3, 4, 2], [1, 4, 2, 3], [1, 4, 3, 2], [2, 1, 3, 4], [2, 1, 4, 3], [2, 3, 1, 4], [2, 3, 4, 1], [2, 4, 1, 3], [2, 4, 3, 1], [3, 1, 2, 4], [3, 1, 4, 2], [3, 2, 1, 4], [3, 2, 4, 1], [3, 4, 1, 2], [3, 4, 2, 1], [4, 1, 2, 3], [4, 1, 3, 2], [4, 2, 1, 3], [4, 2, 3, 1], [4, 3, 1, 2], [4, 3, 2, 1]]");
    }

    /**
     * 全排列
     *
     * @param nums 要求数字不能重复
     */
    public List<List<Integer>> permute(int[] nums) {
        if (null == nums) {
            return Collections.emptyList();
        }
        List<List<Integer>> result = new ArrayList<>();

        permute(result, new ArrayList<>(), OriginJDKUtil.newCopy(nums));
        return result;
    }

    private void permute(List<List<Integer>> result,
                         List<Integer> toBeAddList,
                         List<Integer> remainList) {
        //有限解
        if (1 == remainList.size()) {
            //注意不要破坏源数据结构
            List<Integer> onePermutation = OriginJDKUtil.newCopy(toBeAddList, toBeAddList.size() + 1);
            onePermutation.addAll(remainList);
            result.add(onePermutation);
        }
        //递归解
        for (int i = 0; i < remainList.size(); i++) {
            Integer data = remainList.get(i);
            List<Integer> destToBeAddedList = OriginJDKUtil.newCopy(toBeAddList, toBeAddList.size() + 1);
            destToBeAddedList.add(data);
            List<Integer> destRemainList = OriginJDKUtil.newCopy(remainList, remainList.size());
            destRemainList.remove(i);
            permute(result, destToBeAddedList, destRemainList);
        }

    }
}
