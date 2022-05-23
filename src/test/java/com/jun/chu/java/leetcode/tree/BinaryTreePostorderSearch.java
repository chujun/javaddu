package com.jun.chu.java.leetcode.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树的后序遍历
 * [145. 二叉树的后序遍历](https://leetcode.cn/problems/binary-tree-postorder-traversal/)
 * 二叉树的前序遍历,中序遍历，后续遍历都是针对领头的根节点而言.
 * 根节点最先遍历:中左右->前序遍历
 * 根节点中间遍历：左中右->中序遍历
 * 根节点最后遍历：左右中->后序遍历
 *
 * @author chujun
 * @date 2022/5/21
 */
public class BinaryTreePostorderSearch {
    @Test
    public void test() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        treeNode1.right = treeNode2;
        treeNode2.left = treeNode3;
        List<Integer> result = postorderTraversalForRevert(treeNode1);
        Assert.assertEquals("[3, 2, 1]", result.toString());

    }

    @Test
    public void test2() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        TreeNode treeNode4 = new TreeNode(4);
        TreeNode treeNode5 = new TreeNode(5);
        treeNode1.left = treeNode2;
        treeNode1.right = treeNode4;
        treeNode2.left = treeNode5;
        treeNode2.right = treeNode3;
        List<Integer> result = postorderTraversalForRevert(treeNode1);
        Assert.assertEquals("[5, 3, 2, 4, 1]", result.toString());

    }


    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorderTraversal(result, root);
        return result;
    }

    /**
     * 时间复杂度：O(n)，其中 n 是二叉搜索树的节点数。每一个节点恰好被遍历一次。
     * <p>
     * 空间复杂度：O(n)，为递归过程中栈的开销，平均情况下为 O(logn)，最坏情况下树呈现链状，为 O(n)。
     */
    private void postorderTraversal(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        postorderTraversal(result, treeNode.left);
        postorderTraversal(result, treeNode.right);
        //最后访问中间节点
        result.add(treeNode.val);
    }

    private List<Integer> postorderTraversalForRevert(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorderTraversalForRevert(result, root);
        return result;
    }

    /**
     * 1-->2(左),4(右),2-->5(左),3(右)
     * 后序遍历为：5 3 2 4 1
     * <p>
     * 先序遍历为：1 2 5 3 4
     * <p>
     * 逆后序遍历为：1 4 2 3 5
     * **比较先序遍历(中左右)和逆后续遍历顺序可知,逆后续遍历是中右左,**
     */
    private void postorderTraversalForRevert(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        List<Integer> tmpResult = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.add(treeNode);
        while (!stack.empty()) {
            TreeNode pop = stack.pop();
            tmpResult.add(pop.val);
            if (null != pop.left) {
                stack.push(pop.left);
            }
            if (null != pop.right) {
                stack.push(pop.right);
            }
        }
        //这儿还是有额外的On遍历耗时
        for (int i = tmpResult.size() - 1; i >= 0; i--) {
            result.add(tmpResult.get(i));
        }
    }


}
