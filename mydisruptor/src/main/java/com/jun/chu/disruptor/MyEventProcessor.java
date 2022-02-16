package com.jun.chu.disruptor;

/**
 * @author chujun
 * @date 2022/2/16
 */
public interface MyEventProcessor extends Runnable {
    MySequence getSequence();

    void halt();

    boolean isRunning();
}
