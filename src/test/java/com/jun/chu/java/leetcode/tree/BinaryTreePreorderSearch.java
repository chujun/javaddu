package com.jun.chu.java.leetcode.tree;

import org.junit.Assert;
import org.junit.Before;
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
    private static int counter = 0;
    private static int counter2 = 0;

    @Before
    public void before() {
        counter = 0;
        counter2 = 0;
    }

    @Test
    public void test() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        treeNode1.right = treeNode2;
        treeNode2.left = treeNode3;
        List<Integer> result = preorderTraversalForUnifyStyle(treeNode1);
        Assert.assertEquals("[1, 2, 3]", result.toString());
        Assert.assertEquals(6, counter);
        Assert.assertEquals(9, counter2);//3*2+1+1+1=9

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
        List<Integer> result = preorderTraversalForUnifyStyle(treeNode1);
        Assert.assertEquals("[1, 2, 5, 3, 4]", result.toString());
        Assert.assertEquals(10, counter);
        Assert.assertEquals(15, counter2);//5*2+1+2+2=15
    }

    @Test
    public void test3() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        treeNode1.right = treeNode2;
        List<Integer> result = preorderTraversalForUnifyStyle(treeNode1);
        Assert.assertEquals("[1, 2]", result.toString());
        Assert.assertEquals(4, counter);
        Assert.assertEquals(6, counter2);//2*2+1+1=6

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

    private List<Integer> preorderTraversalForUnifyStyle(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderTraversalForUnifyStyle(result, root);
        return result;
    }

    /**
     * 算法效率不高,写前序遍历，中序遍历，后序遍历风格统一，仅需要调整几行代码位置即可(和递归类似)
     * 核心思想:节点遍历和节点处理分开,
     * 根节点重新入栈处理,
     * 栈内存储两种数据,一种是需要遍历的遍历节点,一种是需要处理的处理节点,
     * 区分办法:需要处理的节点入栈时再push一个空节点用来和需要遍历的节点区分开
     * 总结:弄明白后，算法结构还是比较清晰的，风格比较统一,但是思路不容易想到，主要还是效率较低
     * 压入栈里的数据:n节点数量+n(空节点数量)+1(首节点多入栈一次)+含有左节点数量+含有右节点数量
     * 时间复杂度:(On)=2n
     * <p>
     * <p>
     * 具体算法思想见
     * [二叉树的统一迭代法](https://programmercarl.com/%E4%BA%8C%E5%8F%89%E6%A0%91%E7%9A%84%E7%BB%9F%E4%B8%80%E8%BF%AD%E4%BB%A3%E6%B3%95.html#%E4%BA%8C%E5%8F%89%E6%A0%91%E7%9A%84%E7%BB%9F%E4%B8%80%E8%BF%AD%E4%BB%A3%E6%B3%95)
     */
    private void preorderTraversalForUnifyStyle(List<Integer> result, TreeNode treeNode) {
        if (null == treeNode) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(treeNode);
        counter2++;
        while (!stack.empty()) {
            counter++;
            TreeNode cur = stack.pop();
            if (null != cur) {
                //需要遍历的遍历节点
                //右节点入栈,如果是空节点则不入栈
                if (null != cur.right) {
                    stack.push(cur.right);
                    counter2++;
                }
                //右节点入栈,如果是空节点则不入栈
                if (null != cur.left) {
                    stack.push(cur.left);
                    counter2++;
                }
                stack.push(cur);
                //再push一个null节点用来和需要遍历的节点区标识
                stack.push(null);
                counter2 += 2;
            } else {
                //需要处理的的处理节点
                //空节点弹出后，再弹出真正需要处理的节点
                TreeNode data = stack.pop();
                result.add(data.val);
            }
        }
    }

}
