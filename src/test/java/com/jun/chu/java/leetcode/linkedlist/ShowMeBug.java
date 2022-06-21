package com.jun.chu.java.leetcode.linkedlist;

import org.junit.Test;

/**
 * link1 8->4->7
 * link2 7->7
 * result:5->2->8
 * 链表每个数字个位数,进位向前
 * @author chujun
 * @date 2022/6/21
 */
public class ShowMeBug {
    @Test
    public void test(){
        Node l1 = new Node(3);
        Node p = l1;
        p.next = new Node(5);
        p = p.next;
        p.next = new Node(7);

        Node l2 = new Node(5);
        Node q = l2;
        q.next = new Node(9);

        Solution instance = new Solution();
        Node answer = instance.solution(l1, l2);
        if (answer != null) {
            System.out.print(answer.val);
            answer = answer.next;
        }

        while (answer != null) {
            System.out.print(" -> " + answer.val);
            answer = answer.next;
        }
    }

}
class Node {
    int val;
    Node next;

    public Node(int val) {
        this.val = val;
    }

}

class Solution {
    // 请按你的实际需求修改参数
    public Node solution(Node a, Node b) {
        // 在这⾥书写你的代码
        if (null == a && null == b) {
            return new Node(0);
        }
        if (null == a) {
            return b;
        }
        if (null == b) {
            return a;
        }
        Node result = new Node(0);
        Node indexA = a;
        Node indexB = b;
        Node cur = result;
        int up = 0;
        boolean init = true;

        while ((null != indexA && null != indexB) || up > 0) {
            if (!init) {
                if (null == cur.next) {
                    cur.next = new Node(0);
                    cur = cur.next;
                }
            }
            int value = up;
            if (null != indexA) {
                value += indexA.val;
                indexA = indexA.next;
            }
            if (null != indexB) {
                value += indexB.val;
                indexB = indexB.next;
            }
            if (value > 10) {
                cur.val = value % 10 + up;
                up = 1;
            } else {
                cur.val = value % 10;
                up = 0;
            }
            init = false;
        }
        return result;
    }
}

