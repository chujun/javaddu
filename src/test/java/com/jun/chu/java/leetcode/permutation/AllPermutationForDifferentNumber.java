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
    public void testPermuteV2() {
        System.out.println(permuteV2(new int[]{1}));
        System.out.println(permuteV2(new int[]{1, 2}));
        System.out.println(permuteV2(new int[]{1, 2, 3}));
        List<List<Integer>> result = permuteV2(new int[]{1, 2, 3, 4});
        System.out.println(result);
        System.out.println(result.size());
        Assert.assertEquals(permuteV2(new int[]{1}).toString(), "[[1]]");
        Assert.assertEquals(permuteV2(new int[]{1, 2}).toString(), "[[1, 2], [2, 1]]");
        Assert.assertEquals(permuteV2(new int[]{1, 2, 3}).toString(), "[[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]");
        Assert.assertEquals(permuteV2(new int[]{1, 2, 3, 4}).toString(), "[[1, 2, 3, 4], [1, 2, 4, 3], [1, 3, 2, 4], [1, 3, 4, 2], [1, 4, 2, 3], [1, 4, 3, 2], [2, 1, 3, 4], [2, 1, 4, 3], [2, 3, 1, 4], [2, 3, 4, 1], [2, 4, 1, 3], [2, 4, 3, 1], [3, 1, 2, 4], [3, 1, 4, 2], [3, 2, 1, 4], [3, 2, 4, 1], [3, 4, 1, 2], [3, 4, 2, 1], [4, 1, 2, 3], [4, 1, 3, 2], [4, 2, 1, 3], [4, 2, 3, 1], [4, 3, 1, 2], [4, 3, 2, 1]]");
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
            List<Integer> onePermutation = OriginJDKUtil.newCopy(toBeAddList, toBeAddList.size() + 1);
            onePermutation.addAll(remainList);
            result.add(onePermutation);
            return;
        }
        //递归解
        for (int i = 0; i < remainList.size(); i++) {
            Integer data = remainList.get(i);
            //这儿有copy成本
            List<Integer> destToBeAddedList = OriginJDKUtil.newCopy(toBeAddList, toBeAddList.size() + 1);
            destToBeAddedList.add(data);
            //这儿有copy成本
            List<Integer> destRemainList = OriginJDKUtil.newCopy(remainList, remainList.size());
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
        return tree.toList();
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

    public static class Tree<T> {
        private TreeNode<T> rootNode;

        private boolean rootNodeEmpty;

        public Tree(final TreeNode<T> rootNode, final boolean rootNodeEmpty) {
            this.rootNode = rootNode;
            this.rootNodeEmpty = rootNodeEmpty;
        }

        public List<List<T>> toList() {
            if (null == rootNode) {
                return Collections.emptyList();
            }

        }
    }

    public static class TreeNode<T> {
        private T num;
        private List<TreeNode<T>> nextNodes;

        public TreeNode(final T num) {
            this.num = num;
        }

        public TreeNode(final T num, final List<TreeNode<T>> nextNodes) {
            this.num = num;
            this.nextNodes = nextNodes;
        }

        public void addNode(TreeNode<T> treeNode) {
            if (null == nextNodes) {
                nextNodes = new ArrayList<>();
            }
            nextNodes.add(treeNode);
        }

        public T getNum() {
            return num;
        }

        public List<TreeNode<T>> getNextNodes() {
            return nextNodes;
        }
    }
}
