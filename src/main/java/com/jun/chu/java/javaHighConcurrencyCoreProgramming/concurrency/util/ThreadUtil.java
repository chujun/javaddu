package com.jun.chu.java.javaHighConcurrencyCoreProgramming.concurrency.util;

import com.jun.chu.java.javaHighConcurrencyCoreProgramming.concurrency.util.ShutdownHookThread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 来自book-Java高并发核心编程.卷2
 *
 * @author chujun
 * @date 2021/7/9
 */
public class ThreadUtil {

    /**
     * 定制的线程工厂
     */
    private static class CustomThreadFactory implements ThreadFactory {
        //线程池数量
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;

        //线程数量
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String threadTag;

        CustomThreadFactory(String threadTag) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
            this.threadTag = "apppool-" + poolNumber.getAndIncrement() + "-" + threadTag + "-";
        }

        @Override
        public Thread newThread(Runnable target) {
            Thread t = new Thread(group, target,
                threadTag + threadNumber.getAndIncrement(),
                0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    //懒汉式单例创建线程池：用于定时任务、顺序排队执行任务
    static class SeqOrScheduledTargetThreadPoolLazyHolder {
        //线程池：用于定时任务、顺序排队执行任务
        static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(
            1,
            new CustomThreadFactory("seq"));

        static {
            //JVM关闭时的钩子函数
            Runtime.getRuntime().addShutdownHook(
                new ShutdownHookThread("定时和顺序任务线程池", new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        //优雅关闭线程池
                        shutdownThreadPoolGracefully(EXECUTOR);
                        return null;
                    }
                }));
        }

    }

    /**
     * 优雅关闭线程池
     * 该方法实现参考ExecutorService源码头文档
     *
     * @param threadPool 线程池
     */
    public static void shutdownThreadPoolGracefully(ExecutorService threadPool) {
        if (!(threadPool instanceof ExecutorService) || threadPool.isTerminated()) {
            return;
        }
        try {
            threadPool.shutdown();   //拒绝接受新任务
        } catch (SecurityException e) {
            System.err.println("线程池任务未正常执行结束" + e);
            return;
        } catch (NullPointerException e) {
            System.err.println("线程池任务未正常执行结束" + e);
            return;
        }
        try {
            // 等待 60 s，等待线程池中的任务完成执行
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                // 调用 shutdownNow 取消正在执行的任务
                threadPool.shutdownNow();
                // 再次等待 60 s，如果还未结束，可以再次尝试，或则直接放弃
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("线程池任务未正常执行结束");
                }
            }
        } catch (InterruptedException ie) {
            // 捕获异常，重新调用 shutdownNow
            threadPool.shutdownNow();
        }
        //任然没有关闭，循环关闭1000次，每次等待10毫秒
        if (!threadPool.isTerminated()) {
            try {
                for (int i = 0; i < 1000; i++) {
                    if (threadPool.awaitTermination(10, TimeUnit.MILLISECONDS)) {
                        break;
                    }
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            } catch (Throwable e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * 顺序排队执行
     *
     * @param command
     */
    public static void seqExecute(Runnable command) {
        getSeqOrScheduledExecutorService().execute(command);
    }


    /**
     * 获取可调度线程池（包含提交延迟、定时、周期性、顺序性执行的任务）
     *
     * @return
     */
    public static ScheduledThreadPoolExecutor getSeqOrScheduledExecutorService() {
        return SeqOrScheduledTargetThreadPoolLazyHolder.EXECUTOR;
    }


    /**
     * 线程睡眠
     *
     * @param millisecond 毫秒
     */
    public static void sleepMilliSeconds(int millisecond)
    {
        LockSupport.parkNanos(millisecond * 1000L * 1000L);
    }

    /**
     * 获取当前线程名称
     */
    public static String getCurThreadName()
    {
        return Thread.currentThread().getName();
    }
}
