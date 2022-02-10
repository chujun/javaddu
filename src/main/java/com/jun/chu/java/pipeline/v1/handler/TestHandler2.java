package com.jun.chu.java.pipeline.v1.handler;

import com.jun.chu.java.pipeline.v1.Handler;
import com.jun.chu.java.pipeline.v1.HandlerContext;

/**
 * @author chujun
 * @date 2022/2/10
 */
public class TestHandler2 implements Handler {
    @Override
    public void channelRead(HandlerContext ctx, Object msg) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = (String) msg + "-handler2";
        System.out.println(result);
        ctx.write(result);
    }
}
