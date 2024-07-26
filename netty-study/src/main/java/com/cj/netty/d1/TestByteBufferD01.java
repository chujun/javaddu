package com.cj.netty.d1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件通道FileChannel
 *
 * @author chujun
 * @date 2024/7/26
 */
@Slf4j
public class TestByteBufferD01 {
    public static void main(String[] args) {
        //FileChannel
        //1.输入输出流 2.RandomAccessFile
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            //准备缓存区
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                int len = channel.read(buffer);
                log.debug("读取到的字节 {}",len);
                if (-1 == len) {
                    break;
                }
                //读buffer内容
                //切换至读模式
                buffer.flip();
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    log.debug("实际字节 {}",(char) b);
                }
                //切换至写模式
                buffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
