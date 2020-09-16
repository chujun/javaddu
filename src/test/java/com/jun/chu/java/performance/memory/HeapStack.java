package com.jun.chu.java.performance.memory;

/**
 * 验证在栈中分配内存比堆中更快
 * @link https://github.com/russelltao/geektime_distrib_perf/blob/master/2-memory/benchmark/heap_stack.java
 * @author chujun
 * @date 2020-09-15
 */
public class HeapStack {
    public static void main(String[] args) {
        //1024*1024 后者比前者快
        //1024*1024*1024 前者比后者快
        //TODO:cj 并不是那么简单
        long loopCount = 1024*1024;
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            alloc();
        }
        long t2 = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            alloc_stack();
        }
        long t3 = System.currentTimeMillis();
        System.out.println("时间消耗：" + (t2 - t1) + "," + (t3-t2) + " 毫秒\n");
    }


    private static void alloc() {
        User user = new User();
    }

    private static void alloc_stack() {
        User user;
    }
    static class User {
    }
}
