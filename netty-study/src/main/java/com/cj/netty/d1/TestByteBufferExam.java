package com.cj.netty.d1;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.cj.netty.util.ByteBufferUtil.debugAll;

/**
 * ByteBuffer综合示例
 * 需求背景:网络传输过程中存在粘包,半包
 * 原始数据为3条
 * Hello World\n
 * I'm zhangsan\n
 * How are you\n
 * <p>
 * 变成了下面两个ByteBuffer
 * Hello World\nI'm zhangsan\nHo
 * w are you\n
 * 现在要求你把两个ByteBuffer恢复成原始的按\n分割的数据
 *
 * @author chujun
 * @date 2024/7/29
 */
@Slf4j
public class TestByteBufferExam {
    private static List<ByteBuffer> result = Lists.newArrayList();

    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello World\nI'm zhangsan\nHo".getBytes(StandardCharsets.UTF_8));
        debugAll(source);
        split(source);
        source.put("w are you\n".getBytes(StandardCharsets.UTF_8));
        split(source);
    }

    private static void split(final ByteBuffer source) {
        //切换到读模式
        source.flip();

        int length = 0;

        log.debug("\\n:{}", Integer.toHexString('\n'));
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) != (byte) '\n') {
                //这儿存在遍历,效率比较低
                length++;
            } else {
                //把完成消息存入新ByteBuffer,新ByteBuffer长度计算
                //debugAll(source);
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                //移除\n
                source.get();
                result.add(target);
                debugAll(target);
                length = 0;
            }
        }

        //切换到写模式,不能从头写,所以不用clear
        source.compact();

    }
}
