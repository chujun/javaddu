package com.jun.chu.java.mulithread;

import java.util.HashMap;
import java.util.Map;

/**
 * 指令重排序可能对并发的影响分析
 *
 * @author chujun
 * @date 2022/6/5
 */
public class VolatileInstructionResort {
    private Map<String, String> conf;
    //此变量必须用volatile,会有指令重排序问题，这儿内存可见性问题反倒不是问题,大不了多sleep几次。
    volatile boolean initialized = false;

    //线程A执行
    private void init() {
        conf = new HashMap<>();
        //进行其他初始化配置
        processInitConfig(conf);
        //initialized状态变量设置为true,告诉其他线程配置可用了
        initialized = true;
    }

    private void processInitConfig(final Map<String, String> conf) {

    }

    //线程B执行
    private void execute() throws InterruptedException {
        //等待配置初始化完成，在进行后续其他操作
        while (!initialized) {
            Thread.sleep(100);
        }

        doSomethingWithConf();
    }

    /**
     * 方法约定:配置完成后才能掉该方法，不然会抛出异常
     */
    private void doSomethingWithConf() {

    }
}
