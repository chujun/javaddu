package com.jun.chu.disruptor;

/**
 * 不属于核心api系列
 * @author chujun
 * @date 2022/2/17
 */
public interface MyEventTranslatorThreeArg<T, A, B, C> {
    void translateTo(T event, long sequence, A arg0, B arg1, C arg2);
}
