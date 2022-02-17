package com.jun.chu.disruptor;

/**
 * publishEvent vs tryPublishEvent
 * MyEventTranslator 参数类型变种:无参，1个参数，2个参数，3个参数，数组
 * 单个事件发布 vs 批量事件发布
 *
 * @author chujun
 * @date 2022/2/17
 */
public interface MyEventSink<E> {
    void publishEvent(MyEventTranslator<E> translator);

    boolean tryPublishEvent(MyEventTranslator<E> translator);

    <A> void publishEvent(MyEventTranslatorOneArg<E, A> translator, A arg0);

    <A> boolean tryPublishEvent(MyEventTranslatorOneArg<E, A> translator, A arg0);

    <A, B> void publishEvent(MyEventTranslatorTwoArg<E, A, B> translator, A arg0, B arg1);

    <A, B> boolean tryPublishEvent(MyEventTranslatorTwoArg<E, A, B> translator, A arg0, B arg1);

    <A, B, C> void publishEvent(MyEventTranslatorThreeArg<E, A, B, C> translator, A arg0, B arg1, C arg2);

    <A, B, C> boolean tryPublishEvent(MyEventTranslatorThreeArg<E, A, B, C> translator, A arg0, B arg1, C arg2);

    void publishEvent(MyEventTranslatorVararg<E> translator, Object... args);

    boolean tryPublishEvent(MyEventTranslatorVararg<E> translator, Object... args);

    void publishEvents(MyEventTranslator<E>[] translators);

    void publishEvents(MyEventTranslator<E>[] translators, int batchStartsAt, int batchSize);

    boolean tryPublishEvents(MyEventTranslator<E>[] translators);

    boolean tryPublishEvents(MyEventTranslator<E>[] translators, int batchStartsAt, int batchSize);

    <A> void publishEvents(MyEventTranslatorOneArg<E, A> translator, A[] arg0);

    <A> void publishEvents(MyEventTranslatorOneArg<E, A> translator, int batchStartsAt, int batchSize, A[] arg0);

    <A> boolean tryPublishEvents(MyEventTranslatorOneArg<E, A> translator, A[] arg0);

    <A> boolean tryPublishEvents(MyEventTranslatorOneArg<E, A> translator, int batchStartsAt, int batchSize, A[] arg0);

    <A, B> void publishEvents(MyEventTranslatorTwoArg<E, A, B> translator, A[] arg0, B[] arg1);

    <A, B> void publishEvents(
        MyEventTranslatorTwoArg<E, A, B> translator, int batchStartsAt, int batchSize, A[] arg0,
        B[] arg1);

    <A, B> boolean tryPublishEvents(MyEventTranslatorTwoArg<E, A, B> translator, A[] arg0, B[] arg1);

    <A, B> boolean tryPublishEvents(
        MyEventTranslatorTwoArg<E, A, B> translator, int batchStartsAt, int batchSize,
        A[] arg0, B[] arg1);

    <A, B, C> void publishEvents(MyEventTranslatorThreeArg<E, A, B, C> translator, A[] arg0, B[] arg1, C[] arg2);


    <A, B, C> void publishEvents(
        MyEventTranslatorThreeArg<E, A, B, C> translator, int batchStartsAt, int batchSize,
        A[] arg0, B[] arg1, C[] arg2);

    <A, B, C> boolean tryPublishEvents(MyEventTranslatorThreeArg<E, A, B, C> translator, A[] arg0, B[] arg1, C[] arg2);

    <A, B, C> boolean tryPublishEvents(
        MyEventTranslatorThreeArg<E, A, B, C> translator, int batchStartsAt,
        int batchSize, A[] arg0, B[] arg1, C[] arg2);


    void publishEvents(MyEventTranslatorVararg<E> translator, Object[]... args);

    void publishEvents(MyEventTranslatorVararg<E> translator, int batchStartsAt, int batchSize, Object[]... args);

    boolean tryPublishEvents(MyEventTranslatorVararg<E> translator, Object[]... args);

    boolean tryPublishEvents(MyEventTranslatorVararg<E> translator, int batchStartsAt, int batchSize, Object[]... args);
}
