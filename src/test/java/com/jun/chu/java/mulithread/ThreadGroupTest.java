package com.jun.chu.java.mulithread;

import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author chujun
 * @date 2021/9/6
 */
public class ThreadGroupTest {
    @Test
    public void test(){
        // 获取 Java 线程管理 MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的 monitor 和 synchronizer 信息，仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        // 遍历线程信息，仅打印线程 ID 和线程名称信息
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
        }
    }
    @Test
    public void test02() {
        // 创建一个线程,不指定线程组,由系统自动分配
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Object local = new Object();
                synchronized (local) {
                    try {
                        local.wait();
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }

                }
            }
        });
        // 设置线程名称
        thread1.setName("Thread1");

        // 创建一个线程组
        ThreadGroup threadGroup = new ThreadGroup("MyThreadGroup");
        // 指定创建一个线程,并且指定线程组
        Thread thread2 = new Thread(threadGroup, new Runnable() {
            @Override
            public void run() {
                Object local = new Object();
                synchronized (local) {
                    try {
                        local.wait();
                    } catch (InterruptedException e) {
                        //不处理异常
                    }

                }
            }
        }, "MyThread2");

        // 启动两个线程
        thread1.start();
        thread2.start();

        ThreadGroup current = Thread.currentThread().getThreadGroup();
        current.list();
        // 获取主线程的父线程组
        ThreadGroup parentThreadGroup = Thread.currentThread().getThreadGroup().getParent();
        // 将有关此线程组的信息打印到标准输出。此方法仅对调试有用。
        parentThreadGroup.list();
    }
/*
以下是打印的结果
java.lang.ThreadGroup[name=system,maxpri=10]
    Thread[Reference Handler,10,system]
    Thread[Finalizer,8,system]
    Thread[Signal Dispatcher,9,system]
    Thread[Attach Listener,5,system]
    java.lang.ThreadGroup[name=main,maxpri=10]
        Thread[main,5,main]
        Thread[Monitor Ctrl-Break,5,main]
        Thread[Thread1,5,main]
        java.lang.ThreadGroup[name=MyThreadGroup,maxpri=10]
            Thread[MyThread2,5,MyThreadGroup]
*/
}
