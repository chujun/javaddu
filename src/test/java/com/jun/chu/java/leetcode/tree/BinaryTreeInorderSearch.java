package com.jun.chu.java.leetcode.tree;

import org.junit.Assert;
import org.junit.Before;
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
    private static int counter = 0;

    @Before
    public void before() {
        counter = 0;
    }

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
        List<Integer> result = inorderTraversalForIteratorWithoutPushRootNodeInit(treeNode1);
        Assert.assertEquals("[1, 3, 2]", result.toString());
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderTraversal(result, root);
        return result;
    }

    /**
     * 递归方法
     */
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

    private List<Integer> inorderTraversalForIteratorWithoutPushRootNodeInit(TreeNode treeNode) {
        List<Integer> result = new ArrayList<>();
        inorderTraversalForIteratorWithoutPushRootNodeInit(result, treeNode);
        return result;
    }

    /**
     * 相比inorderTraversalForIterator方法 stack初始时不压入根节点
     * 单层遍历逻辑:
     * 中节点不为空则栈压入中节点,遍历左节点(循环直至左节点为空)；
     * 中节点为空，则弹出栈顶节点,遍历节点值,开始遍历右节点
     * <p>
     * 时间复杂度：O(n)，其中 n 为二叉树节点的个数。二叉树的遍历中每个节点会被访问一次且只会被访问一次。
     * <p>
     * 空间复杂度：O(n)。空间复杂度取决于栈深度，而栈深度在二叉树为一条链的情况下会达到 O(n) 的级别
     *
     * @see BinaryTreeInorderSearch#inorderTraversalForIterator(TreeNode)
     */
    private void inorderTraversalForIteratorWithoutPushRootNodeInit(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        //stack不压入根节点
        TreeNode cur = treeNode;
        //null != cur是debug中当中发现NPE才补上的,根节点访问完后栈内就没有元素了，但此时右节点还没有遍历
        while (!stack.isEmpty() || null != cur) {
            while (null != cur) {
                //迭代节点发现左叶子的处理(左子节点先一直访问完)
                stack.push(cur);
                cur = cur.left;
            }
            //迭代节点没有发现左叶子的处理
            TreeNode pop = stack.pop();
            result.add(pop.val);//相比前序遍历的同类方法唯一区别就在于这儿，访问根节点的数据时机
            //处理右叶子
            cur = pop.right;
        }
    }


}
