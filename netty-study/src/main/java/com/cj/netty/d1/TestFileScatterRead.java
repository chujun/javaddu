package com.cj.netty.d1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.cj.netty.util.ByteBufferUtil.debugAll;

/**
 * 对FileChannel文件内容做分散读取到不同ByteBuffer
 *
 * @author chujun
 * @date 2024/7/27
 */
public class TestFileScatterRead {
    public static void main(String[] args) {
        try (FileChannel channel = new RandomAccessFile("word.txt", "r").getChannel()) {
            ByteBuffer byteBufferA = ByteBuffer.allocate(3);
            ByteBuffer byteBufferB = ByteBuffer.allocate(3);
            ByteBuffer byteBufferC = ByteBuffer.allocate(5);
            channel.read(new ByteBuffer[]{byteBufferA, byteBufferB, byteBufferC});
            //切换读模式
            byteBufferA.flip();
            byteBufferB.flip();
            byteBufferC.flip();
            debugAll(byteBufferA);
            debugAll(byteBufferB);
            debugAll(byteBufferC);
        } catch (IOException e) {
        }
    }
}
