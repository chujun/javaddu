package com.cj.netty.d1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * FileChannel集中写入
 *
 * @author chujun
 * @date 2024/7/29
 */
@Slf4j
public class TestGatheringWrite {
    public static void main(String[] args) {
        ByteBuffer a = StandardCharsets.UTF_8.encode("hello");
        ByteBuffer b = StandardCharsets.UTF_8.encode("world");
        ByteBuffer c = StandardCharsets.UTF_8.encode("你好");
        try (FileChannel fileChannel = new RandomAccessFile("word2.txt", "rw").getChannel()) {
            fileChannel.write(new ByteBuffer[]{a, b, c});
            log.debug("file length {}",fileChannel.size());
        } catch (IOException e) {
        }
    }
}
