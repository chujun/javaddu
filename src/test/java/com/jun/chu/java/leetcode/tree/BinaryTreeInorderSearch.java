package com.jun.chu.java.leetcode.tree;

import java.util.ArrayList;
import java.util.List;

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

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

}
