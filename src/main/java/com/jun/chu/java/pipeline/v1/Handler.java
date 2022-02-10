package com.jun.chu.java.pipeline.v1;

/**
 * @author chujun
 * @date 2022/2/10
 */
public interface Handler {
    /**
     * 该接口只需要实现字符串处理，然后把结果传给下一个handler就可以了
     * @param ctx 接口参数HandlerContext为下一个handler的context
     * @param msg Object为从上一个handler处理之后传入的结果。
     */
    void channelRead(HandlerContext ctx, Object msg);
}
