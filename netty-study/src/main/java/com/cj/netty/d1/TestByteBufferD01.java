package com.cj.netty.d1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * nio用ByteBuffer读文件数据
 * 文件通道FileChannel
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
                //从channel中读取数据，写入buffer
                int len = channel.read(buffer);
                log.debug("读取到的字节数 {}",len);
                if (-1 == len) {//读取不到内容了
                    break;
                }
                //读buffer内容

                //buffer切换至读模式
                buffer.flip();
                while (buffer.hasRemaining()) {//buffer是否还有剩余未读数据
                    byte b = buffer.get();
                    log.debug("实际字节 {}",(char) b);
                }
                //buffer切换至写模式clear/compact
                buffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
