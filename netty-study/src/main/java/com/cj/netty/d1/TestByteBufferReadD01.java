package com.cj.netty.d1;

import com.cj.netty.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;


/**
 * ByteBuffer读数据
 * @author chujun
 * @date 2024/7/27
 */
@Slf4j
public class TestByteBufferReadD01 {
    public static void main(String[] args){
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        byteBuffer.put(new byte[]{'a','b','c','d'});

        //切换读模式
        byteBuffer.flip();
        byteBuffer.get(new byte[4]);
        ByteBufferUtil.debugAll(byteBuffer);
        //rewind重头开始读
        byteBuffer.rewind();
        log.debug("{}",byteBuffer.get());//a

        //mark && reset
        log.debug("{}",byteBuffer.get());//b
        log.debug("{}",byteBuffer.get());//c
        byteBuffer.mark();//加标记,3的位置
        log.debug("{}",byteBuffer.get());//d
        byteBuffer.reset();//重回标记位置3
        log.debug("{}",byteBuffer.get());//d

        //get(i)
        byteBuffer.rewind();
        ByteBufferUtil.debugAll(byteBuffer);
        log.debug("{}",byteBuffer.get(2));//d
        ByteBufferUtil.debugAll(byteBuffer);//不会改变索引位置
    }
}
