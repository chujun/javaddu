package com.jun.chu.disruptor;

/**
 * 不属于核心api系列
 * @author chujun
 * @date 2022/2/17
 */
public interface MyEventTranslatorVararg<T> {
    void translateTo(T event, long sequence, Object... args);
}
