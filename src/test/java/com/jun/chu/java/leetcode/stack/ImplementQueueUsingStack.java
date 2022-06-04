package com.jun.chu.java.leetcode.stack;

import org.junit.Assert;
import org.junit.Test;

import java.util.Stack;

/**
 * 用栈实现队列
 * [232 用栈实现队列](https://leetcode.cn/problems/implement-queue-using-stacks/)
 * 思路:用两个栈模拟实现队列，一个入栈，一个出栈
 * 使用两个栈 入队 - O(1)，出队 - 摊还复杂度 O(1)
 * 摊还复杂度分析
 * 可以不用计算分摊时间复杂度，直接考虑每个元素即可，
 * 无论是哪个元素，都是入栈两次(进一次A和一次B)，出栈两次(同理)，所以摊还复杂度都是O（1)
 * <p>
 * 题目有重要前提假设：假设所有操作都是有效的 （例如，一个空的队列不会调用 pop 或者 peek 操作）
 *
 * @author chujun
 * @date 2022/6/4
 */
public class ImplementQueueUsingStack {
    @Test
    public void test() {
        MyQueue myQueue = new MyQueue();
        myQueue.push(1);
        myQueue.push(2);
        Assert.assertEquals(1, myQueue.pop());
        myQueue.push(3);
        myQueue.push(4);
        Assert.assertEquals(2, myQueue.pop());
        Assert.assertEquals(3, myQueue.pop());
        Assert.assertEquals(4, myQueue.peek());
        Assert.assertEquals(4, myQueue.pop());
        Assert.assertTrue(myQueue.empty());
    }

    class MyQueue {
        //入栈
        private Stack inStack;
        //出栈
        private Stack outStack;

        public MyQueue() {
            inStack = new Stack();
            outStack = new Stack();
        }

        public void push(int x) {
            inStack.push(x);
        }

        /**
         * 题目有重要前提假设：
         * 假设所有操作都是有效的 （例如，一个空的队列不会调用 pop 或者 peek 操作）
         */
        public int pop() {
            dumpStackIn();
            return (int) outStack.pop();
        }

        /**
         * 题目有重要前提假设：
         * 假设所有操作都是有效的 （例如，一个空的队列不会调用 pop 或者 peek 操作）
         */
        public int peek() {
            dumpStackIn();
            return (int) outStack.peek();
        }

        public boolean empty() {
            return inStack.isEmpty() && outStack.isEmpty();
        }

        // 如果outStack为空，那么将inStack中的元素全部放到outStack中
        private void dumpStackIn() {
            if (!outStack.isEmpty()) return;
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
    }
}


