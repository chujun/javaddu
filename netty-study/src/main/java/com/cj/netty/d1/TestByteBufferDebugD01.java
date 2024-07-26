package com.cj.netty.d1;

import com.cj.netty.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static com.cj.netty.util.ByteBufferUtil.debugAll;

/**
 * 用ByteBuffer测试工具打印展示ByteBuffer内容
 * position: [4], limit: [30]
 *          +-------------------------------------------------+
 *          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 * +--------+-------------------------------------------------+----------------+
 * |00000000| 61 62 63 64 00 00 00 00 00 00 00 00 00 00 00 00 |abcd............|
 * |00000010| 00 00 00 00 00 00 00 00 00 00 00 00 00 00       |..............  |
 * +--------+-------------------------------------------------+----------------+
 * @author chujun
 * @date 2024/7/26
 */
@Slf4j
public class TestByteBufferDebugD01 {
    public static void main(String[] args){
        ByteBuffer byteBuffer = ByteBuffer.allocate(30);
        byteBuffer.put((byte)0x61);//a
        debugAll(byteBuffer);

        byteBuffer.put(new byte[]{0x62,0x63,0x64});//b,c,d
        debugAll(byteBuffer);
        //log.debug("读不到正确数据:{}",byteBuffer.get());
        byteBuffer.flip();

        log.debug("读取数据:{}",byteBuffer.get());
        debugAll(byteBuffer);

        byteBuffer.compact();
        //不清零,abc往前移动，多了一个64,没关系，下次写入第二个64被覆盖
        debugAll(byteBuffer);
        byteBuffer.put(new byte[]{0x65,0x66});//e,f
        debugAll(byteBuffer);


    }
}
