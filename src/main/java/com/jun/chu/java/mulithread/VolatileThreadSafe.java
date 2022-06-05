package com.jun.chu.java.mulithread;

/**
 * @author chujun
 * @date 2022/6/5
 */
public class VolatileThreadSafe {
    //状态变量,0表示初始化，+1表示启动，-1表示停止，boolean shutdown当然也可以
    private volatile int ctl;

    private void shutdown() {
        ctl = -1;
    }

    private void start() {
        ctl = 1;
    }

    private void execute() {
        while (ctl > 0) {
            //dowork
        }
    }
}
