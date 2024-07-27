package com.cj.netty.d1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

import static com.cj.netty.util.ByteBufferUtil.debugAll;

/**
 * ByteBuffer与String互转
 *
 * @author chujun
 * @date 2024/7/27
 */
@Slf4j
public class TestByteBufferString {
    public static void main(String[] args) {
        //1.字符串转ByteBuffer
        ByteBuffer byteBuffer0 = ByteBuffer.allocate(16);
        byteBuffer0.put("hello0".getBytes(StandardCharsets.UTF_8));
        debugAll(byteBuffer0);


        //2.Charset
        ByteBuffer byteBuffer1 = StandardCharsets.UTF_8.encode("hello1");
        debugAll(byteBuffer1);//自动切换成读模式 position: [0], limit: [6]
//        while (byteBuffer1.hasRemaining()) {
//            log.debug("{}",byteBuffer1.get());
//        }
//        debugAll(byteBuffer1);

        //3.wrap方法
        ByteBuffer byteBuffer2 = ByteBuffer.wrap("hello3".getBytes());
        debugAll(byteBuffer2);//自动切换成读模式 position: [0], limit: [6]

        //ByteBuffer转String
        String s2 = StandardCharsets.UTF_8.decode(byteBuffer2).toString();
        log.debug("{}",s2);

        String s1 = StandardCharsets.UTF_8.decode(byteBuffer1).toString();
        log.debug("{}",s1);

        byteBuffer0.flip();
        String s = StandardCharsets.UTF_8.decode(byteBuffer0).toString();
        log.debug("{}",s);
    }
}
