package com.jun.chu.java.pipeline.v1;

import com.jun.chu.java.pipeline.v1.handler.TestHandler1;
import com.jun.chu.java.pipeline.v1.handler.TestHandler2;

/**
 * @author chujun
 * @date 2022/2/10
 */
public class TestMain {
    public static void main(String[] args) {
        //先定义一个pipeline，然后在pipeline中添加两个handler，然后为了测试并发性能，分别请求多次。
        MyPipeline pipeline = new MyPipeline();
        pipeline.addFirst(new TestHandler2());//添加handler1
        pipeline.addFirst(new TestHandler1());//添加handler2
        for (int i = 0; i < 10; i++) {//提交多个任务
            pipeline.Request("hello" + i);
        }
    }
}
/**
 * 输出结果
 * hello3-handler1
 * hello1-handler1
 * hello2-handler1
 * hello0-handler1
 * hello4-handler1
 * hello5-handler1
 * hello7-handler1
 * hello9-handler1
 * hello8-handler1
 * hello6-handler1
 * hello3-handler1-handler2
 * hello7-handler1-handler2
 * hello2-handler1-handler2
 * hello8-handler1-handler2
 * hello9-handler1-handler2
 * hello0-handler1-handler2
 * hello1-handler1-handler2
 * hello5-handler1-handler2
 * hello6-handler1-handler2
 * hello4-handler1-handler2
 */