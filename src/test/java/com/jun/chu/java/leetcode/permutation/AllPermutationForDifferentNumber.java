package com.jun.chu.java.leetcode.permutation;

import com.jun.chu.java.leetcode.tools.OriginJDKUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 针对不重复数字组合的全排列
 * [leetcode46 全排列](https://leetcode.cn/problems/permutations/)
 *
 * @author chujun
 * @date 2022/5/19
 */
public class AllPermutationForDifferentNumber {
    private static int costCounter1 = 0;
    private static int costCounter2 = 0;
    private static int costCounter3 = 0;

    @Test
    public void testCost() {
        //时间复杂度估算
        costCounter1 = 0;
        costCounter2 = 0;
        costCounter3 = 0;
        Assert.assertEquals(permute(new int[]{1, 2, 3, 4}).toString(), "[[1, 2, 3, 4], [1, 2, 4, 3], [1, 3, 2, 4], [1, 3, 4, 2], [1, 4, 2, 3], [1, 4, 3, 2], [2, 1, 3, 4], [2, 1, 4, 3], [2, 3, 1, 4], [2, 3, 4, 1], [2, 4, 1, 3], [2, 4, 3, 1], [3, 1, 2, 4], [3, 1, 4, 2], [3, 2, 1, 4], [3, 2, 4, 1], [3, 4, 1, 2], [3, 4, 2, 1], [4, 1, 2, 3], [4, 1, 3, 2], [4, 2, 1, 3], [4, 2, 3, 1], [4, 3, 1, 2], [4, 3, 2, 1]]");
        //(4*3*2*1)*(4-1)=24*3
        Assert.assertEquals(24 * 3, costCounter1);
        //4*0+(4*3)*1+(4*3*2)*2
        Assert.assertEquals(60, costCounter2);
        //4*4+(4*3)*3+(4*3*2)*2
        Assert.assertEquals(100, costCounter3);
    }

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

    @Test
    public void testPermuteV10() {
        Assert.assertEquals(permuteV10(new int[]{1}).toString(), "[[1]]");
        Assert.assertEquals(permuteV10(new int[]{1, 2}).toString(), "[[1, 2], [2, 1]]");
        Assert.assertEquals(permuteV10(new int[]{1, 2, 3}).toString(), "[[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 2, 1], [3, 1, 2]]");
        Assert.assertEquals(permuteV10(new int[]{1, 2, 3, 4}).toString(), "[[1, 2, 3, 4], [1, 2, 4, 3], [1, 3, 2, 4], [1, 3, 4, 2], [1, 4, 3, 2], [1, 4, 2, 3], [2, 1, 3, 4], [2, 1, 4, 3], [2, 3, 1, 4], [2, 3, 4, 1], [2, 4, 3, 1], [2, 4, 1, 3], [3, 2, 1, 4], [3, 2, 4, 1], [3, 1, 2, 4], [3, 1, 4, 2], [3, 4, 1, 2], [3, 4, 2, 1], [4, 2, 3, 1], [4, 2, 1, 3], [4, 3, 2, 1], [4, 3, 1, 2], [4, 1, 3, 2], [4, 1, 2, 3]]");
    }


    /////////////////////////////方法一暴力遍历\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

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

        permute(result, new ArrayList<>(), OriginJDKUtil.newCopyList(nums));
        return result;
    }

    private void permute(List<List<Integer>> result,
                         List<Integer> toBeAddList,
                         List<Integer> remainList) {
        if (0 == remainList.size()) {
            return;
        }
        //有限解
        if (1 == remainList.size()) {
            //注意不要破坏源数据结构
            //这儿有copy成本
            costCounter1 += toBeAddList.size();
            List<Integer> onePermutation = OriginJDKUtil.newCopy(toBeAddList, toBeAddList.size() + 1);
            //可优化,linkedList比arraylist更有优势,这儿不存在随机读取，都是顺序读取
            onePermutation.addAll(remainList);
            result.add(onePermutation);

            return;
        }
        //递归解
        for (int i = 0; i < remainList.size(); i++) {
            Integer data = remainList.get(i);
            //这儿有copy成本
            costCounter2 += toBeAddList.size();
            List<Integer> destToBeAddedList = OriginJDKUtil.newCopy(toBeAddList, toBeAddList.size() + 1);
            //costCounter2没统计这儿的成本,可优化,linkedList比arraylist更有优势,这儿不存在随机读取，都是顺序读取
            destToBeAddedList.add(data);

            //这儿有copy成本
            costCounter3 += remainList.size();
            List<Integer> destRemainList = OriginJDKUtil.newCopy(remainList, remainList.size());
            //costCounter3没统计这儿的成本,可优化,linkedList比arraylist更有优势,这儿不存在随机读取，都是顺序读取
            destRemainList.remove(i);
            permute(result, destToBeAddedList, destRemainList);
        }
    }

    /////////////////////////////方法一暴力遍历\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /////////////////////////////方法二使用树结构简化复制成本\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * 使用树结构存储中间计算过程，减少复制成本
     */
    private List<List<Integer>> permuteV2(int[] nums) {
        if (null == nums || 0 == nums.length) {
            return Collections.emptyList();
        }
        TreeNode<Integer> currentNode = new TreeNode<>(null);
        Tree<Integer> tree = new Tree<>(currentNode, true);
        permuteV2(currentNode, OriginJDKUtil.newCopySet(nums));
        //这儿树的遍历成本增加了
        return tree.outputTraverseTrace();
    }

    private void permuteV2(TreeNode<Integer> node,
                           Set<Integer> remainSet) {
        if (0 == remainSet.size()) {
            return;
        }
        //有限解
        if (1 == remainSet.size()) {
            TreeNode<Integer> leftNode = new TreeNode<>(remainSet.stream().findFirst().orElse(null));
            node.addNode(leftNode);
            return;
        }
        for (Integer data : remainSet) {
            TreeNode<Integer> currentNode = new TreeNode<>(data);
            node.addNode(currentNode);
            //这儿有copy成本
            Set<Integer> destRemainSet = OriginJDKUtil.newCopy(remainSet);
            destRemainSet.remove(data);
            permuteV2(currentNode, destRemainSet);
        }
    }

    /////////////////////////////方法二使用树结构简化复制成本\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /////////////////////////////方法十直接基于数组本身，不需要复制成本\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * 最佳推荐
     * [参考官方实现](https://leetcode.cn/problems/permutations/solution/quan-pai-lie-by-leetcode-solution-2/)
     * 参考官方实现，直接基于数据结构,使用状态重置替代复制(成本耗费巨大)
     * 数组结构划分成两部分,左边已遍历数组|右边待遍历数组,[1,2,3|4,5,6,7]
     * 时间复杂度:O(n!*n):n!,遍历节点数量,输出结果n到结果集
     * 空间复杂度:
     */
    public List<List<Integer>> permuteV10(int[] nums) {
        if (null == nums || 0 == nums.length) {
            return Collections.emptyList();
        }
        List<List<Integer>> result = new ArrayList<>();
        permuteV10(result, 0, nums);
        return result;
    }

    private void permuteV10(List<List<Integer>> result,
                            int traverseLocation,
                            int[] nums) {
        //有限确定解
        if (traverseLocation == nums.length - 1) {
            List<Integer> onePermutation = OriginJDKUtil.newCopyList(nums);
            result.add(onePermutation);
            return;
        }
        for (int i = traverseLocation; i < nums.length; i++) {
            //swap,交换两个下标位置的数值
            if (i != traverseLocation) {
                swap(nums, traverseLocation, i);
            }
            //递归遍历下一层
            permuteV10(result, 1 + traverseLocation, nums);
            //revert,状态重置(这是回溯算法深度遍历的关键),重新交换回来两个下标位置的数值
            //然后数组就恢复原状态了，这儿很神奇
            if (i != traverseLocation) {
                swap(nums, i, traverseLocation);
            }
        }
    }

    private void swap(final int[] nums, final int traverseLocation, final int i) {
        int a = nums[traverseLocation];
        nums[traverseLocation] = nums[i];
        nums[i] = a;
    }

    /////////////////////////////方法十直接基于数组本身，不需要复制成本\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public static class Tree<T> {
        private TreeNode<T> rootNode;

        private boolean rootNodeEmpty;

        public Tree(final TreeNode<T> rootNode, final boolean rootNodeEmpty) {
            this.rootNode = rootNode;
            this.rootNodeEmpty = rootNodeEmpty;
        }

        //输出遍历轨迹,不成环的遍历
        public List<List<T>> outputTraverseTrace() {
            if (null == rootNode) {
                return Collections.emptyList();
            }
            //TODO:cj to be done,其实也不方便这儿还涉及到树的深度遍历了，路径处理
            return null;
        }
    }

    public static class TreeNode<T> {
        private T data;
        private List<TreeNode<T>> nextNodes;

        public TreeNode(final T data) {
            this.data = data;
        }

        public TreeNode(final T data, final List<TreeNode<T>> nextNodes) {
            this.data = data;
            this.nextNodes = nextNodes;
        }

        public void addNode(TreeNode<T> treeNode) {
            if (null == nextNodes) {
                nextNodes = new ArrayList<>();
            }
            nextNodes.add(treeNode);
        }

        public T getData() {
            return data;
        }

        public List<TreeNode<T>> getNextNodes() {
            return nextNodes;
        }

    }
}
