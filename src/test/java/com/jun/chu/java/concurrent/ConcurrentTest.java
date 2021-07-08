package com.jun.chu.java.concurrent;

/**
 * come from book 《java并发编程的艺术》 第一章
 * 多线程执行效率一定高嘛
 * 单线程 VS 多线程
 *
 * 本机环境
 * MacBook Pro (13-inch, 2017, Two Thunderbolt 3 ports)
 * 版本10.15.7 (19H1217)
 * 2.3 GHz 双核Intel Core i5
 * 8 GB 2133 MHz LPDDR3
 *
 * 本机测试结果比较
 * |count|concurrent|serial|比较|
 * |--|--|--|--|
 * |1万|5ms|<1ms|并发慢|
 * |10万|4ms|4ms|差不多|
 * |100万|12ms|6ms|并发慢|
 * |1000万|19ms|16ms|差不多|
 * |1亿|88ms|151ms|并发快|
 * @author chujun
 * @date 2021/7/8
 */
public class ConcurrentTest {
    private static final long count = 100000000l;

    public static void main(String[] args) throws InterruptedException {
        concurrency();
        serial();
    }

    private static void concurrency() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int a = 0;
                for (long i = 0; i < count; i++) {
                    a += 5;
                }
                System.out.println(a);
            }
        });
        thread.start();
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        thread.join();
        long time = System.currentTimeMillis() - start;
        System.out.println("concurrency :" + time + "ms,b=" + b);
    }

    private static void serial() {
        long start = System.currentTimeMillis();
        int a = 0;
        for (long i = 0; i < count; i++) {
            a += 5;
        }
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("serial:" + time + "ms,b=" + b + ",a=" + a);
    }
}
