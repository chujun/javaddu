package com.jun.chu.java.leetcode.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * [翻转二叉树](https://leetcode.cn/problems/invert-binary-tree/)
 *
 * @author chujun
 * @date 2022/6/4
 */
public class InvertBinaryTree {
    @Test
    public void test() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        treeNode1.right = treeNode2;
        treeNode2.left = treeNode3;
        invertTree(treeNode1);
        BinaryTreePreorderSearch search = new BinaryTreePreorderSearch();
        List<Integer> result = search.preorderTraversal(treeNode1);
        Assert.assertEquals("[1, 2, 3]", result.toString());

    }

    public TreeNode invertTree(TreeNode root) {
        invertTreeRecursive(root);
        return root;
    }

    private void invertTreeRecursive(TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        //中
        swapLeftAndRightTreeNode(treeNode);
        //左
        invertTreeRecursive(treeNode.left);
        //右
        invertTreeRecursive(treeNode.right);
    }

    private void swapLeftAndRightTreeNode(TreeNode treeNode) {
        TreeNode tmp = treeNode.left;
        treeNode.left = treeNode.right;
        treeNode.right = tmp;
    }
}
