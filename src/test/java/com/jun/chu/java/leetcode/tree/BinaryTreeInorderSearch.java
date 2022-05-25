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
        List<Integer> result = preorderTraversalForMorris(treeNode1);
        Assert.assertEquals("[1, 3, 2]", result.toString());
    }

    @Test
    public void testInorderTraversalForIteratorV100() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        treeNode1.right = treeNode2;
        treeNode2.left = treeNode3;
        List<Integer> result = preorderTraversalForMorris(treeNode1);
        Assert.assertEquals("[1, 3, 2]", result.toString());
    }

    public List<Integer> inorderTraversal(TreeNode treeNode) {
        List<Integer> result = new ArrayList<>();
        inorderTraversal(result, treeNode);
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

    private List<Integer> inorderTraversalForIterator(TreeNode treeNode) {
        List<Integer> result = new ArrayList<>();
        inorderTraversalForIterator(result, treeNode);
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

    private List<Integer> inorderTraversalForUnifyStyle(TreeNode treeNode) {
        List<Integer> result = new ArrayList<>();
        inorderTraversalForUnifyStyle(result, treeNode);
        return result;
    }

    /**
     * 见前序遍历对应算法描述
     */
    private void inorderTraversalForUnifyStyle(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(treeNode);
        while (!stack.empty()) {
            TreeNode cur = stack.pop();
            if (null != cur) {
                //需要遍历的遍历节点
                //右节点入栈,如果是空节点则不入栈
                if (null != cur.right) {
                    stack.push(cur.right);
                }
                //和前序遍历算法不同之处仅仅在于根节点的代码位置调整了
                stack.push(cur);
                //再push一个null节点用来和需要遍历的节点区标识
                stack.push(null);

                //左节点入栈,如果是空节点则不入栈
                if (null != cur.left) {
                    stack.push(cur.left);
                }

            } else {
                //需要处理的的处理节点
                //空节点弹出后，再弹出真正需要处理的节点
                TreeNode data = stack.pop();
                result.add(data.val);
            }
        }
    }

    private List<Integer> preorderTraversalForMorris(TreeNode treeNode) {
        List<Integer> result = new ArrayList<>();
        preorderTraversalForMorris(result, treeNode);
        return result;
    }

    /**
     * <p>
     * 针对中序遍历访问数据时机:
     * 根节点不存在左子节点时访问节点数据
     */
    private void preorderTraversalForMorris(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        TreeNode root = treeNode;
        TreeNode cur;
        while (null != root) {

            cur = root.left;
            //左子节点存在
            if (null != cur) {
                //如果根节点的左子节点存在,一直遍历寻找左子节点的最右叶子节点(C右)
                //结束条件(a.right指针为null.没有建立临时链接 b.right指针指向根节点，已经建立了临时链接）
                while (null != cur.right && cur.right != root) {
                    cur = cur.right;
                }
                if (null == cur.right) {
                    //right指针为null.没有建立临时链接
                    //节点right(原本指针为null)指向根节点，创建临时链接
                    cur.right = root;
                    root = root.left;
                    continue;
                } else {
                    //right指针指向根节点，已经建立了临时链接
                    //断开到根节点的这个临时链接
                    cur.right = null;
                }
            } else {
                //左子节点不存在
            }
            result.add(root.val);
            root = root.right;
        }
    }


}
