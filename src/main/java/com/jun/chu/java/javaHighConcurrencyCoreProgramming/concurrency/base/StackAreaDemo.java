package com.jun.chu.java.javaHighConcurrencyCoreProgramming.concurrency.base;

import com.jun.chu.java.javaHighConcurrencyCoreProgramming.concurrency.util.Print;

/**
 * 来自book-Java高并发核心编程.卷2
 * 第一章 线程区域信息
 *
 * @author chujun
 * @date 2021/7/9
 */
public class StackAreaDemo {
    public static void main(String[] args) throws InterruptedException {
        Print.cfo("当前线程名称：" + Thread.currentThread().getName());
        Print.cfo("当前线程ID：" + Thread.currentThread().getId());
        Print.cfo("当前线程状态：" + Thread.currentThread().getState());
        Print.cfo("当前线程优先级：" + Thread.currentThread().getPriority());
        int a = 1, b = 1;
        int c = a / b;
        anotherFun();
        Thread.sleep(5000);
        System.out.println("end");
    }

    private static void anotherFun() {
        int a = 1, b = 1;
        int c = a / b;
        anotherFun2();
    }

    private static void anotherFun2() {
        int a = 1, b = 1;
        int c = a / b;
    }
}
