package com.jun.chu.disruptor;

/**
 * 新型方式发布event，封装老式发布事件三步走过程,对api使用者而言更加友好
 * 如下为老式方法
 * <p>
 * ```
 * long sequence = ringBuffer.next();
 * try {
 * <p>
 * MyEvent myEvent = ringBuffer.get(sequence);
 * // 放置数据
 * myEvent.setValue(i);
 * } finally {
 * // 提交，如果不提交完成事件会一直阻塞
 * ringBuffer.publish(sequence);
 * }
 * ```
 * <p>
 *
 * @author chujun
 * @date 2022/2/17
 */
public interface MyEventTranslator<T> {
    void translateTo(T event, long sequence);
}
