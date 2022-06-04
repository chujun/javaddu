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

    @Test
    public void testPreorderTraversalForMorris() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        treeNode1.right = treeNode2;
        treeNode2.left = treeNode3;
        List<Integer> result = preorderTraversalForMorris(treeNode1);
        Assert.assertEquals("[1, 2, 3]", result.toString());

    }

    @Test
    public void test2PreorderTraversalForMorris() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        TreeNode treeNode4 = new TreeNode(4);
        TreeNode treeNode5 = new TreeNode(5);
        treeNode1.left = treeNode2;
        treeNode1.right = treeNode4;
        treeNode2.left = treeNode5;
        treeNode2.right = treeNode3;
        List<Integer> result = preorderTraversalForMorris(treeNode1);
        Assert.assertEquals("[1, 2, 5, 3, 4]", result.toString());
    }

    @Test
    public void test3PreorderTraversalForMorris() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        treeNode1.right = treeNode2;
        List<Integer> result = preorderTraversalForMorris(treeNode1);
        Assert.assertEquals("[1, 2]", result.toString());

    }


    public List<Integer> preorderTraversal(TreeNode treeNode) {
        List<Integer> result = new ArrayList<>();
        preorderTraversal(result, treeNode);
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

    public List<Integer> preorderTraversalForIterator(TreeNode treeNode) {
        List<Integer> result = new ArrayList<>();
        preorderTraversalForIterator(result, treeNode);
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

    public List<Integer> preorderTraversalForIteratorWithoutPushRootNodeInit(TreeNode treeNode) {
        List<Integer> result = new ArrayList<>();
        preorderTraversalForIteratorWithoutPushRootNodeInit(result, treeNode);
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

    private List<Integer> preorderTraversalForUnifyStyle(TreeNode treeNode) {
        List<Integer> result = new ArrayList<>();
        preorderTraversalForUnifyStyle(result, treeNode);
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
                //左节点入栈,如果是空节点则不入栈
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
                //空节点弹出后，再弹出真正需要处理的节点,这儿不会有空栈异常(因为空节点后必有节点)
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
     * morris遍历,最大特色就是空间复杂度为常量,利用二叉树中的叶子节点null指针指向下一个需要遍历的节点
     * 临时创建一个链接,从左子节点的最右子节点的right指向根节点,后续再断开临时链接
     * 核心通用思路(不同之处仅在于访问节点数据的时机):
     * 1. 如果根节点的左子节点存在,一直遍历寻找左子节点的最右叶子节点(C右)
     * 结束条件(a.right指针为null.没有建立临时链接 b.right指针指向根节点，已经建立了临时链接）
     * 1.1如果(C右)节点不存在，则左子节点本身就是最右叶子节点(C右)
     * 1.2如果(C右)节点存在,则(C右)节点right(原本指针为null)指向根节点，创建临时链接.
     * 然后遍历根节点的左子节点(根节点重新指向根节点的左子节点)
     * <p>
     * 2. 如果根节点的左子节点不存在,继续遍历根节点的右节点
     * 3.断开临时链接
     * 针对1而言如果根节点的左子节点存在,一直遍历寻找左子节点的最右叶子节点(C右)，
     * 如果C右节点发现right指针指向根节点本身一样时(临时链接建立的指向),那么断开这个临时链接,
     * 然后继续遍历根节点的右子节点(左子树已经遍历完成了)（根节点重新指向根节点的右子节点)
     * <p>
     * 最后二叉树的最右叶子节点最后遍历
     * <p>
     * 根节点会遍历两次左子节点,第一次建立指向自身的临时链接,第二次断开临时链接
     * <p>
     * <p>
     * 时间复杂度O(n)
     * 空间复杂度O(1)
     * 缺点:虽然遍历结束时二叉树结构不变，但是遍历过程中修改了二叉树的结构，线程不安全
     * [前序遍历Morris解法](https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/leetcodesuan-fa-xiu-lian-dong-hua-yan-shi-xbian-2/)
     * <p>
     * 针对前序遍历访问数据时机:
     * 根节点存在左子节点时建立链接时访问根节点数据
     * 根节点不存在左子节点时(根节点本身就是上一层的左节点)访问节点数据
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
                //出while循环的两种情况分析
                if (null == cur.right) {
                    //right指针为null.没有建立临时链接
                    result.add(root.val);
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
                result.add(root.val);
            }
            root = root.right;
        }
    }

}
