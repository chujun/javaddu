package com.jun.chu.java.mulithread.executorservice;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

/**
 * @author 00065906
 * @date 2022/8/8
 */
public class ExecutorServiceTest {
    private int threadPoolSize=3;
    private ExecutorService executorService=new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 60L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<Runnable>(threadPoolSize*10),(r, executor) -> {
        try {
            executor.getQueue().put(r);
        } catch (InterruptedException e) {
            System.out.println("重新加入任务异常："+e);
        }
    });

    private int maxEmptyTimes=10;

    @Test
    public void test(){
        //当count等于100时，超过线程池最大任务数会出现死循环现象。
        for(int count=0;count<10;count++){
            System.out.println("create task"+count);
            execute(count);
        }
    }

    private void execute(final int count) {
        executorService.submit(()->{
            long emptyTimes = 0;
            //高并发下这儿会存在死循环问题
            while (!canPull()){
                System.out.println("线程池队列已满,线程yield"+count);
                applyWait(emptyTimes ++);
            }
            System.out.println("execute task"+count);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("mock task finished"+count);
        });
    }

    private void applyWait(long emptyTimes) {
        long newEmptyTimes = emptyTimes > maxEmptyTimes ? maxEmptyTimes : emptyTimes;
        if (emptyTimes <= 3) { // 3次以内
            Thread.yield();
        } else { // 超过3次，最多只sleep 10ms
            LockSupport.parkNanos(1000 * 1000L * newEmptyTimes);
        }
    }
    private boolean canPull(){
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)executorService;
        if(threadPoolExecutor.getQueue().isEmpty() ||
            (threadPoolExecutor.getQueue().remainingCapacity()>0)){
            return true;
        }
        return false;
    }
}
