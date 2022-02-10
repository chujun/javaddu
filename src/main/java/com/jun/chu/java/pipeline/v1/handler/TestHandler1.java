package com.jun.chu.java.pipeline.v1.handler;

import com.jun.chu.java.pipeline.v1.Handler;
import com.jun.chu.java.pipeline.v1.HandlerContext;

/**
 * @author chujun
 * @date 2022/2/10
 */
public class TestHandler1 implements Handler {
    @Override
    public void channelRead(HandlerContext ctx, Object msg) {
        try {
            Thread.sleep(1000);//模拟阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = (String) msg + "-handler1";//在字符串后面加特定字符串
        System.out.println(result);
        ctx.write(result);//写入操作，这个操作是必须的，相当于将结果传递给下一个handler
    }
}
