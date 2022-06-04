package com.jun.chu.java.leetcode.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * [翻转二叉树](https://leetcode.cn/problems/invert-binary-tree/)
 * 对每一个遍历到的节点，进行的节点操作是翻转左右节点而非简单的取值
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
        if (null == root) {
            return null;
        }
        invertTreePostorderUsingStack(root);
        return root;
    }

    /**
     * 前序遍历
     * <p>
     * 复杂度分析
     * <p>
     * 时间复杂度：O(N)，其中 N为二叉树节点的数目。我们会遍历二叉树中的每一个节点，对每个节点而言，我们在常数时间内交换其两棵子树。
     * <p>
     * 空间复杂度：O(N)。使用的空间由递归栈的深度决定，它等于当前节点在二叉树中的高度。
     * 在平均情况下，二叉树的高度与节点个数为对数关系，即 O(logN)。
     * 而在最坏情况下，树形成链状，空间复杂度为 O(N)。
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


    /**
     * 前序遍历使用栈，根节点先入栈方式,非递归方式
     */
    private void invertTreePreorderUsingStack(TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(treeNode);
        while (!stack.isEmpty()) {
            TreeNode cur = stack.pop();
            System.out.println(cur.val);
            swapLeftAndRightTreeNode(cur);
            if (null != cur.left) {
                stack.push(cur.left);
            }
            if (null != cur.right) {
                stack.push(cur.right);
            }
        }
    }

    /**
     * 中序遍历使用栈
     */
    private void invertTreeInorderUsingStack(TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = treeNode;
        while (null != cur || !stack.isEmpty()) {
            if (null != cur) {
                stack.push(cur);
                cur = cur.left;
                while (null != cur) {
                    stack.push(cur);
                    cur = cur.left;
                }
            } else {

            }
            TreeNode node = stack.pop();
            System.out.println(node.val);
            //其实这个rightPoint指针可以省略掉，但是通过中序遍历算法理解起来更一致
            TreeNode rightPoint = node.right;
            //处理节点
            swapLeftAndRightTreeNode(node);
            if (null != rightPoint) {
                cur = rightPoint;
            }
        }
    }

    /**
     * 后续遍历非递归方式
     */
    private void invertTreePostorderUsingStack(TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = treeNode;
        //上一次访问过的节点
        TreeNode preVisitedRightNode = null;
        while (!stack.isEmpty() || null != cur) {
            while (null != cur) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            //cur不是二次入栈的中节点
            if (null == cur.right || cur.right == preVisitedRightNode) {
                //System.out.println(cur.val); 等中节点访问完再交换左右节点
                swapLeftAndRightTreeNode(cur);
                preVisitedRightNode = cur;
                //cur重置为null，否则一直死循环遍历该节点
                cur = null;
            } else {
                //中节点二次入栈
                stack.push(cur);
                cur = cur.right;
            }
        }
    }


    /**
     * 广度优先算法，层序遍历
     */
    private void invertTreeLevelOrder(TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(treeNode);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.remove();
                swapLeftAndRightTreeNode(cur);
                if (null != cur.right) {
                    queue.add(cur.right);
                }
                if (null != cur.left) {
                    queue.add(cur.left);
                }
            }
        }
    }

    private void swapLeftAndRightTreeNode(TreeNode treeNode) {
        TreeNode tmp = treeNode.left;
        treeNode.left = treeNode.right;
        treeNode.right = tmp;
    }
}
