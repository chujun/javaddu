package com.jun.chu.java.leetcode.queue;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * [225. 用队列实现栈](https://leetcode.cn/problems/implement-stack-using-queues/)
 * 题目前提:每次调用 pop 和 top 都保证栈不为空
 * 用一个双端队列实现模拟功能
 *
 * @author chujun
 * @date 2022/6/4
 */
public class ImplementStackUsingQueue {
    class MyStack {
        private Deque deque;

        public MyStack() {
            deque = new ArrayDeque();
        }

        public void push(int x) {
            deque.add(x);
        }

        public int pop() {
            return (int) deque.removeLast();
        }

        public int top() {
            return (int) deque.getLast();
        }

        public boolean empty() {
            return deque.isEmpty();
        }
    }
}
