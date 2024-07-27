package com.cj.netty.d1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * ByteBuffer分配
 * @author chujun
 * @date 2024/7/27
 */
@Slf4j
public class TestByteBufferAllocateD01 {
    public static void main(String[] args) {
        log.debug("{}", ByteBuffer.allocate(10).getClass());
        log.debug("{}", ByteBuffer.allocateDirect(10).getClass());
        /**
         * HeapByteBuffer: java堆内存,读写效率相对较低,jvm GC影响
         * DirectByteBuffer:直接内存,系统内存,读写效率高(少一次拷贝),无jvm GC影响;分配效率低一些,需要注意手动内存回收
         */
    }
}
