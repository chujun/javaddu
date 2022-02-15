package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/15
 */
public interface MyCursored {
    /**
     * 返回当前游标
     */
    long getCursor();
}
