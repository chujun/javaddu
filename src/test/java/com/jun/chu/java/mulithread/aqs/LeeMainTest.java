package com.jun.chu.java.mulithread.aqs;

import org.junit.Test;

/**
 * @author chujun
 * @date 2022/6/9
 */
public class LeeMainTest {
    static int count = 0;
    static LeeLock leeLock = new LeeLock();

    /**
     * 运行结果:当然也可能Thread先执行,不过每次执行结果必然是20000
     * start count:Thread-0
     * end count:Thread-0
     * start count:Thread-1
     * end count:Thread-1
     * 20000
     *
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {

        Runnable runnable = () -> {
            try {
                leeLock.lock();
                System.out.println("start count:" + Thread.currentThread().getName());
                for (int i = 0; i < 10000; i++) {
                    count++;
                }
                System.out.println("end count:" + Thread.currentThread().getName());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                leeLock.unlock();
            }

        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(count);
    }
}
