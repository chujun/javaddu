package com.jun.chu.java.leetcode.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 二叉树层序遍历
 * 利用队列结构模拟先进先出的层序遍历
 * [102. 二叉树的层序遍历](https://leetcode.cn/problems/binary-tree-level-order-traversal/)
 */
public class BinaryTreeLevelOrderSearch {
    @Test
    public void test(){
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        TreeNode treeNode4 = new TreeNode(4);
        TreeNode treeNode5 = new TreeNode(5);
        treeNode1.left = treeNode2;
        treeNode1.right = treeNode4;
        treeNode2.left = treeNode5;
        treeNode2.right = treeNode3;
        List<Integer> result = levelOrder(treeNode1);
        Assert.assertEquals("[1, 2, 4, 5, 3]", result.toString());
    }
    public List<Integer> levelOrder(TreeNode treeNode) {
        List<Integer> result = new ArrayList<>();
        levelOrderTraversal(result, treeNode);
        return result;
    }

    private void levelOrderTraversalForRecursive(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        //TODO:cj to be done

        if (null != treeNode.left) {
            result.add(treeNode.left.val);
        }
        if (null != treeNode.right) {
            result.add(treeNode.right.val);
        }
    }

    private void levelOrderTraversal(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(treeNode);
        while (!queue.isEmpty()) {
            TreeNode queueFirstNode = queue.remove();
            result.add(queueFirstNode.val);
            if (null != queueFirstNode.left) {
                queue.add(queueFirstNode.left);
            }
            if (null != queueFirstNode.right) {
                queue.add(queueFirstNode.right);
            }
        }
    }


}
