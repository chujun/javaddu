package com.jun.chu.java.leetcode.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树的前序遍历
 * [144. 二叉树的前序遍历](https://leetcode.cn/problems/binary-tree-preorder-traversal/)
 * 二叉树的前序遍历,中序遍历，后续遍历都是针对领头的根节点而言.
 * 根节点最先遍历:中左右->前序遍历
 * 根节点中间遍历：左中右->中序遍历
 * 根节点最后遍历：左右中->后序遍历
 *
 * @author chujun
 * @date 2022/5/21
 */
public class BinaryTreePreorderSearch {
    @Test
    public void test() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        treeNode1.right = treeNode2;
        treeNode2.left = treeNode3;
        List<Integer> result = preorderTraversalForIteratorWithoutPushRootNodeInit(treeNode1);
        Assert.assertEquals("[1, 2, 3]", result.toString());
    }

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderTraversal(result, root);
        return result;
    }

    /**
     * 递归方法
     */
    private void preorderTraversal(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        //先访问中间节点
        result.add(treeNode.val);
        preorderTraversal(result, treeNode.left);
        preorderTraversal(result, treeNode.right);
    }

    public List<Integer> preorderTraversalForIterator(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderTraversalForIterator(result, root);
        return result;
    }

    /**
     * 以迭代方式遍历(非递归方式)
     * 前序遍历:
     * 节点遍历顺序:中左右
     * 入栈顺序:中右左，
     * 不用遍历指针的方式，代码看起来很简洁
     */
    private void preorderTraversalForIterator(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(treeNode);
        while (!stack.isEmpty()) {
            TreeNode pop = stack.pop();
            result.add(pop.val);
            //右节点先入栈,后出栈
            if (null != pop.right) {
                stack.push(pop.right);
            }
            //右节点后入栈,先出栈
            if (null != pop.left) {
                stack.push(pop.left);
            }
        }
    }

    public List<Integer> preorderTraversalForIteratorWithoutPushRootNodeInit(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderTraversalForIteratorWithoutPushRootNodeInit(result, root);
        return result;
    }

    /**
     * 根节点先不压入栈方式
     * 单层遍历逻辑:
     * 中节点不为空则访问中节点值,栈压入中节点,遍历左节点(循环直至左节点为空)；
     * 中节点为空，则弹出栈顶节点,开始遍历右节点
     */
    private void preorderTraversalForIteratorWithoutPushRootNodeInit(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        //遍历指针
        TreeNode cur = treeNode;
        while (null != cur || !stack.empty()) {
            while (null != cur) {
                //节点存在情况下(左子节点先一直访问完)
                result.add(cur.val);
                stack.push(cur);
                //一直循环遍历左节点直至左节点为空
                cur = cur.left;
            }
            //节点不存在情况下弹出栈顶,遍历右节点
            TreeNode pop = stack.pop();
            cur = pop.right;
        }
    }

}
