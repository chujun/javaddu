package com.jun.chu.java.pipeline.v1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HandlerContext的作用比较大，首先它是链表的一部分，因此需要有指向下一个context的指针；
 * 然后它负责调用handler，而我们要实现一个并发的处理程序，那么HandlerContext就需要维护一个线程池来供handler处理
 *
 * @author chujun
 * @date 2022/2/10
 */
public class HandlerContext {
    private ExecutorService executor = Executors.newCachedThreadPool();//线程池
    private Handler handler;
    private HandlerContext next;//下一个context的引用

    public HandlerContext(Handler handler) {
        this.handler = handler;
    }

    public void setNext(HandlerContext ctx) {
        this.next = ctx;
    }

    public void doWork(Object msg) {//执行任务的时候向线程池提交一个runnable的任务，任务中调用handler
        if (next == null) {
            return;
        } else {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    handler.channelRead(next, msg);//把下一个handler的context传给handler来实现回调
                }
            });
        }
        //handler.channelRead(next,msg);
    }

    public void write(Object msg) {
        //这里的write操作是给handler调用的，实际上是一个回调方法，当handler处理完数据之后，调用一下next的HandlerContext.write，此时就把任务传递给下一个handler了。
        doWork(msg);
    }
}
