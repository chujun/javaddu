package com.jun.chu.java.javaHighConcurrencyCoreProgramming.concurrency.util;

import java.util.Scanner;

/**
 * syn-同步，
 * t-thread，
 * c-content，
 * o-output
 * @author chujun
 * @date 2021/7/9
 */
public class Print {
    /**
     * 带着类名+方法名输出
     *
     * @param s 待输出的字符串形参
     */
    synchronized public static void cfo(Object s)
    {
        String cft = "[" + ReflectionUtil.getNakeCallClassMethod() + "]";
        //提交线程池进行独立输出，使得输出不影响当前线程的执行
        ThreadUtil.seqExecute(() ->
        {
            System.out.println(cft + "：" + s);
        });
    }

    /**
     * 带着线程名+类名+方法名称输出
     *
     * @param s 待输出的字符串形参
     */
    public static void tcfo(Object s)
    {
        String cft = "[" + Thread.currentThread().getName() + "|" + ReflectionUtil.getNakeCallClassMethod() + "]";

        //提交线程池进行独立输出，使得输出不影响当前线程的执行
        ThreadUtil.seqExecute(() ->
        {

            System.out.println(cft + "：" + s);

        });

    }

    /**
     * 编程过程中的提示说明
     *
     * @param s 提示的字符串形参
     */
    public static void hint(Object s)
    {
        Print.tcfo("/--" + s + "--/");
    }
}
