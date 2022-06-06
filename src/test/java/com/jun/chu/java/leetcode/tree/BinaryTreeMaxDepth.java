package com.jun.chu.java.leetcode.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author chujun
 * @date 2022/6/6
 */
public class BinaryTreeMaxDepth {
    @Test
    public void test() {
        TreeNode treeNode = TreeNode.initTree();
        Assert.assertEquals(3, maxDepth(treeNode));
    }

    public int maxDepth(TreeNode root) {
        return getDepthBFS(root);
    }

    private int getDepthRecursive(TreeNode treeNode, int currentDepth) {
        if (null == treeNode) {
            return currentDepth;
        }
        int leftDepth = getDepthRecursive(treeNode.left, currentDepth + 1);
        int rightDepth = getDepthRecursive(treeNode.right, currentDepth + 1);
        return Math.max(leftDepth, rightDepth);
    }

    /**
     * 对getDepthRecursive方法的简化
     */
    private int getDepthRecursive(TreeNode treeNode) {
        if (null == treeNode) {
            return 0;
        }
        int leftDepth = getDepthRecursive(treeNode.left);
        int rightDepth = getDepthRecursive(treeNode.right);
        return 1 + Math.max(leftDepth, rightDepth);
    }

    /**
     * 层序遍历
     */
    private int getDepthBFS(TreeNode treeNode) {
        if (null == treeNode) {
            return 0;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(treeNode);
        int maxDepth = 0;
        TreeNode cur;
        while (!queue.isEmpty()) {
            maxDepth++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                cur = queue.remove();
                if (null != cur.left) {
                    queue.add(cur.left);
                }
                if (null != cur.right) {
                    queue.add(cur.right);
                }
            }
        }
        return maxDepth;
    }
}
