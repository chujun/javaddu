package com.jun.chu.disruptor;


/**
 * TODO:cj 解释并得不好,
 * Sequence协作屏障用于追踪发布者的游标和依赖的 MyEventProcessor 们的sequence，用于处理数据
 *
 * @author chujun
 * @date 2022/2/15
 */
public interface MySequenceBarrier {
    /**
     * 等待指定的sequence可消费
     * 核心方法
     * @param sequence
     * @return
     * @throws MyAlertException     if a status change has occurred for the Disruptor
     * @throws InterruptedException if the thread needs awaking on a condition variable.
     * @throws MyTimeoutException   if a timeout occurs while waiting for the supplied sequence.
     */
    long waitFor(long sequence) throws MyAlertException, InterruptedException, MyTimeoutException;

    /**
     * 获取当前游标
     *
     * @return value of the cursor for entries that have been published.
     */
    long getCursor();

    /**
     * 判断当前屏障是否处于alert状态
     *
     * @return true if in alert otherwise false.
     */
    boolean isAlerted();

    /**
     * 警告 MyEventProcessor 状态变更，保持这个状态直到被清除
     */
    void alert();

    /**
     * 清楚警告状态
     */
    void clearAlert();

    /**
     * 检查是否处于alert状态，如果是抛出MyAlertException异常
     *
     * @throws MyAlertException
     */
    void checkAlert() throws MyAlertException;

}
