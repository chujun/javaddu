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
    //使用层序遍历输出结果
    private static BinaryTreeLevelOrderSearch search = new BinaryTreeLevelOrderSearch();

    @Test
    public void test() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        treeNode1.right = treeNode2;
        treeNode2.left = treeNode3;
        invertTree(treeNode1);
        List<Integer> result = search.levelOrderTraversalForList(treeNode1);
        Assert.assertEquals("[1, 2, 3]", result.toString());

    }

    @Test
    public void test2() {
        TreeNode root = initTree();
        invertTree(root);
        List<Integer> result = search.levelOrderTraversalForList(root);
        Assert.assertEquals("[4, 7, 2, 9, 6, 3, 1]", result.toString());

    }

    private TreeNode initTree() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        TreeNode treeNode4 = new TreeNode(4);
        TreeNode treeNode6 = new TreeNode(6);
        TreeNode treeNode7 = new TreeNode(7);
        TreeNode treeNode9 = new TreeNode(9);
        treeNode4.left = treeNode2;
        treeNode4.right = treeNode7;
        treeNode2.left = treeNode1;
        treeNode2.right = treeNode3;
        treeNode7.left = treeNode6;
        treeNode7.right = treeNode9;
        return treeNode4;
    }

    public TreeNode invertTree(TreeNode root) {
        invertTreePostorderRecursive(root);
        return root;
    }

    /**
     * 前序遍历
     */
    private void invertTreePreorderRecursive(TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        //中
        swapLeftAndRightTreeNode(treeNode);
        //左
        invertTreePreorderRecursive(treeNode.left);
        //右
        invertTreePreorderRecursive(treeNode.right);
    }

    /**
     * 中序遍历
     */
    private void invertTreeInorderRecursive(TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        //左
        invertTreeInorderRecursive(treeNode.left);
        //中
        swapLeftAndRightTreeNode(treeNode);
        //右
        invertTreeInorderRecursive(treeNode.left);
    }

    /**
     * 后序遍历
     */
    private void invertTreePostorderRecursive(TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        //左
        invertTreeInorderRecursive(treeNode.left);
        //右
        invertTreeInorderRecursive(treeNode.right);
        //中
        swapLeftAndRightTreeNode(treeNode);
    }

    private void swapLeftAndRightTreeNode(TreeNode treeNode) {
        TreeNode tmp = treeNode.left;
        treeNode.left = treeNode.right;
        treeNode.right = tmp;
    }
}
