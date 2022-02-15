package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/15
 */
public interface MySequenced {
    /**
     * 返回数据结构持有条目的容量
     */
    int getBufferSize();

    /**
     * TODO:cj 关键方法
     * 判断数据结构是否有有空间分配指定容量sequence
     * 并发访问方法,返回的值仅能做快照
     */
    boolean hasAvailableCapacity(int requiredCapacity);

    /**
     * 计算剩余可存储容量
     *
     * @return The number of slots remaining.
     */
    long remainingCapacity();

    /**
     * 索取下一个可发布的序列号sequence，和publish(long)方法搭配使用
     * 阻塞式方法，一直尝试索取直到获取到
     *
     * @return the claimed sequence value
     */
    long next();

    /**
     * 批量索取下n个可发布的序列号sequence，一般用于批量发布场景,需要注意的是和publish(long,long)方法搭配使用
     * <pre>
     *      int n = 10;
     *      long hi = sequencer.next(n);
     *      long lo = hi - (n - 1);
     *      for (long sequence = lo; sequence &lt;= hi; sequence++) {
     *          // Do work.
     *      }
     *      sequencer.publish(lo, hi);
     * </pre>
     */
    long next(int n);

    /**
     * 尝试下一个要可发布的序列号sequence，和publish(long)方法搭配使用
     *
     * @return 返回最大值的序列号
     * @throws MyInsufficientCapacityException 如果获取失败则抛出该异常
     */
    long tryNext() throws MyInsufficientCapacityException;


    /**
     * 批量索取下n个可发布的序列号sequence，一般用于批量发布场景,需要注意的是和publish(long,long)方法搭配使用
     *
     * @throws MyInsufficientCapacityException 如果获取失败则抛出该异常
     */
    long tryNext(int n) throws MyInsufficientCapacityException;

    /**
     * 发布指定序列号sequnce事件，在事件完成填充后回调
     * 阻塞式方法，一直尝试索取直到获取到
     */
    void publish(long sequence);

    /**
     * 批量发布序列号事件列表,在所有事件完成值填充后回调
     *
     * @param lo first sequence number to publish
     * @param hi last sequence number to publish
     */
    void publish(long lo, long hi);
}
