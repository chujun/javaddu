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
    public void testForList() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        TreeNode treeNode4 = new TreeNode(4);
        TreeNode treeNode5 = new TreeNode(5);
        treeNode1.left = treeNode2;
        treeNode1.right = treeNode4;
        treeNode2.left = treeNode5;
        treeNode2.right = treeNode3;
        List<Integer> result = levelOrderTraversalForList(treeNode1);
        Assert.assertEquals("[1, 2, 4, 5, 3]", result.toString());
    }

    @Test
    public void test() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        TreeNode treeNode4 = new TreeNode(4);
        TreeNode treeNode5 = new TreeNode(5);
        treeNode1.left = treeNode2;
        treeNode1.right = treeNode4;
        treeNode2.left = treeNode5;
        treeNode2.right = treeNode3;
        List<List<Integer>> list = levelOrderTraversal(treeNode1);
        Assert.assertEquals("[[1], [2, 4], [5, 3]]", list.toString());
    }


    private List<List<Integer>> levelOrderTraversal(TreeNode treeNode) {
        List<List<Integer>> result = new ArrayList<>();
        levelOrderTraversal(result, treeNode);
        return result;
    }

    /**
     * BFS，广度遍历搜索,
     * 对每一层进行遍历,每一层遍历该层节点数，而非一个个地遍历
     */
    private void levelOrderTraversal(List<List<Integer>> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(treeNode);

        while (!queue.isEmpty()) {
            //记录当前这一层有多少个节点
            int size = queue.size();
            List<Integer> itemList = new LinkedList<>();
            while (size > 0) {
                TreeNode poll = queue.poll();
                itemList.add(poll.val);
                if (null != poll.left) {
                    queue.offer(poll.left);
                }
                if (null != poll.right) {
                    queue.offer(poll.right);
                }
                size--;
            }
            result.add(itemList);
        }
    }


    public List<Integer> levelOrderTraversalForList(TreeNode treeNode) {
        List<Integer> result = new ArrayList<>();
        levelOrderTraversalForList(result, treeNode);
        return result;
    }

    /**
     * 注意返回值不同
     */
    private void levelOrderTraversalForList(List<Integer> result, TreeNode treeNode) {
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
