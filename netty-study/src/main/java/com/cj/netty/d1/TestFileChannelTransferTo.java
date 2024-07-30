package com.cj.netty.d1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 测试两个FileChannel相互传输数据
 *
 * @author chujun
 * @date 2024/7/30
 */
@Slf4j
public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        try (FileChannel from = new FileInputStream("data.txt").getChannel();
             FileChannel to = new FileOutputStream("to.txt").getChannel()) {
            //传输效率高,jdk带有了transferTo关键字底层操作系统都使用了零拷贝，
            //transferTo每次文件传输上限:2G数据
            long length = from.size();
            //left变量表示每次剩余多少字节数
            for (long left = length; left > 0; ) {
                log.debug("positon:{},left:{}", length - left, left);
                left -= from.transferTo(length - left, left, to);
            }

        } catch (IOException e) {
        }
    }
}
