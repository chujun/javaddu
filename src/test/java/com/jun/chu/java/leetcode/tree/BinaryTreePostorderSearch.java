package com.jun.chu.java.leetcode.tree;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
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
        List<Integer> result = postorderTraversalV10(treeNode1);
        Assert.assertEquals("[3, 2, 1]", result.toString());
        Assert.assertEquals(4, counter);
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
        List<Integer> result = postorderTraversalV10(treeNode1);
        Assert.assertEquals("[5, 3, 2, 4, 1]", result.toString());
        Assert.assertEquals(7, counter);

    }


    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorderTraversal(result, root);
        return result;
    }

    /**
     * 递归方法
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
        LinkedList<Integer> result = new LinkedList<>();
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
     * 这是一种特殊技巧,利用了中右左遍历顺序和左右中遍历顺序(后序遍历)成互逆的特殊关系处理
     */
    private void postorderTraversalForRevert(LinkedList<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.add(treeNode);
        while (!stack.empty()) {
            TreeNode pop = stack.pop();
            //元素插入头部，这样做的好处是减少一次逆向遍历结果集消耗
            result.addFirst(pop.val);
            if (null != pop.left) {
                //与先序遍历相比,左节点先入栈,后出栈
                stack.push(pop.left);
            }
            if (null != pop.right) {
                //与先序遍历相比,右节点后入栈,先出栈
                stack.push(pop.right);
            }
        }
    }

    public List<Integer> postorderTraversalV10(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorderTraversalV10(result, root);
        return result;
    }


    /**
     * stack里存放的数据量=节点数量n+存在右子节点的节点数量w
     */
    private void postorderTraversalV10(List<Integer> result, TreeNode treeNode) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = treeNode;
        //上一次访问过的节点
        TreeNode prevVisitedNode = null;
        while (cur != null || !stack.empty()) {
            while (null != cur) {
                //(左子节点先一直访问完)
                stack.push(cur);
                counter++;
                cur = cur.left;
            }
            TreeNode pop = stack.pop();
            cur = pop;
            //右节点访问完毕才访问中节点
            if (null == pop.right || pop.right == prevVisitedNode) {
                result.add(pop.val);
                prevVisitedNode = cur;
                //该从栈中取元素了
                cur = null;
            } else {
                //右节点还没有访问,暂时先不访问节点数值,所以这部分中间节点需要重新二次入栈
                stack.push(cur);//这行不加上的话，你会发现结果少了部分中间节点(存在右节点的节点)的遍历
                counter++;
                cur = cur.right;
            }
        }
    }
}
