package com.jun.chu.java.leetcode.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树的中序遍历
 * [94. 二叉树的中序遍历](https://leetcode.cn/problems/binary-tree-inorder-traversal/)
 * 二叉树的前序遍历,中序遍历，后续遍历都是针对领头的根节点而言.
 * 根节点最先遍历:中左右->前序遍历
 * 根节点中间遍历：左中右->中序遍历
 * 根节点最后遍历：左右中->后序遍历
 *
 * @author chujun
 * @date 2022/5/21
 */
public class BinaryTreeInorderSearch {
    @Test
    public void test() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        treeNode1.right = treeNode2;
        treeNode2.left = treeNode3;
        List<Integer> result = inorderTraversalForIterator(treeNode1);
        Assert.assertEquals("[1, 3, 2]", result.toString());
    }

    @Test
    public void testInorderTraversalForIteratorV100() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        treeNode1.right = treeNode2;
        treeNode2.left = treeNode3;
        List<Integer> result = inorderTraversalForIteratorV100(treeNode1);
        Assert.assertEquals("[1, 3, 2]", result.toString());
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderTraversal(result, root);
        return result;
    }

    private void inorderTraversal(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        inorderTraversal(result, treeNode.left);
        //中间访问中间节点
        result.add(treeNode.val);
        inorderTraversal(result, treeNode.right);
    }

    private List<Integer> inorderTraversalForIterator(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderTraversalForIterator(result, root);
        return result;
    }

    /**
     * 以迭代方式遍历(非递归方式),和前序遍历的迭代方式并不是简单的交换前后位置关系即可。
     * 因为迭代顺序和出栈顺序不是相同，不是同时进行的，两者需要分开处理
     * 中序遍历:
     * 节点遍历顺序:左中右
     * 入栈顺序:左右，
     */
    private void inorderTraversalForIterator(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.add(treeNode);
        TreeNode cur = treeNode;
        while (!stack.isEmpty()) {
            if (null != cur.left) {
                //迭代节点发现左叶子的处理
                stack.push(cur.left);
                cur = cur.left;
            } else {
                //迭代节点没有发现左叶子的处理
                TreeNode pop = stack.pop();
                result.add(pop.val);
                if (null != pop.right) {
                    //处理右叶子
                    stack.push(pop.right);
                    cur = pop.right;
                }
            }
        }
    }

    private List<Integer> inorderTraversalForIteratorV100(TreeNode treeNode) {
        List<Integer> result = new ArrayList<>();
        inorderTraversalForIteratorV100(result, treeNode);
        return result;
    }

    /**
     * v100版本相比inorderTraversalForIterator方法 stack初始时不压入根节点
     *
     * @see BinaryTreeInorderSearch#inorderTraversalForIterator(TreeNode)
     */
    private void inorderTraversalForIteratorV100(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        //stack不压入根节点
        TreeNode cur = treeNode;
        do {
            if (null != cur) {
                //迭代节点发现左叶子的处理
                stack.push(cur);
                cur = cur.left;
            } else {
                //迭代节点没有发现左叶子的处理
                TreeNode pop = stack.pop();
                result.add(pop.val);
                //处理右叶子
                cur = pop.right;
            }
            // null != cur是debug中当中发现NPE才补上的,根节点访问完后栈内就没有元素了，但此时右节点还没有遍历
        } while (!stack.isEmpty() || null != cur);
    }


}
