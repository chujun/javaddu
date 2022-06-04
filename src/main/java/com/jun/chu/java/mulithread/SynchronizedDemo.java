package com.jun.chu.java.mulithread;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chujun
 * @date 2022/6/2
 */
public class SynchronizedDemo {
    public void method() {
        synchronized (this) {
            System.out.println("synchronized 代码块");
        }
    }

    public String contractString(String s1, String s2, String s3) {
        return s1 + s2 + s3;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        //存在非后台进程，jvm不会退出
        executorService.submit(() ->
            System.out.println("args = " + Arrays.deepToString(args)));
    }
}
