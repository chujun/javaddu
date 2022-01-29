package com.jun.chu.java.jvm.reference;

/**
 * 开启gc日志 jvm
 * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -Xloggc:/tmp/log/ddu/ReferenceCountGC-gc.txt
 * @author chujun
 * @date 2022/1/29
 */
public class ReferenceCountGC {
    public Object instance = null;
    private static final int _1MB = 1024 * 1024;

    /**
     * 该乘员属性存在意义在于占用一部分内存空间，好让gc日志时候能够观察到
     */
    private byte[] bigSize = new byte[_1MB];

    public static void testGC() {
        ReferenceCountGC a = new ReferenceCountGC();
        ReferenceCountGC b = new ReferenceCountGC();
        a.instance = b;
        b.instance = a;

        a = null;
        b = null;
        //
        System.gc();
    }

    public static void main(String[] args) throws InterruptedException {
        testGC();
        Thread.sleep(10000);
    }
//    Java HotSpot(TM) 64-Bit Server VM (25.211-b12) for bsd-amd64 JRE (1.8.0_211-b12), built on Apr  1 2019 20:53:18 by "java_re" with gcc 4.2.1 (Based on Apple Inc. build 5658) (LLVM build 2336.11.00)
//    Memory: 4k page, physical 16777216k(472004k free)
//
///proc/meminfo:
//
//    CommandLine flags: -XX:InitialHeapSize=268435456 -XX:MaxHeapSize=4294967296 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC
//2022-01-29T15:13:37.649-0800: 0.172: [GC (System.gc()) [PSYoungGen: 6306K->624K(76288K)] 6306K->632K(251392K), 0.0018768 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
//        2022-01-29T15:13:37.651-0800: 0.174: [Full GC (System.gc()) [PSYoungGen: 624K->0K(76288K)] [ParOldGen: 8K->451K(175104K)] 632K->451K(251392K), [Metaspace: 2925K->2925K(1056768K)], 0.0075688 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
//
//
//
//
//    Heap
//    PSYoungGen      total 76288K, used 5900K [0x000000076ab00000, 0x0000000770000000, 0x00000007c0000000)
//    eden space 65536K, 9% used [0x000000076ab00000,0x000000076b0c3088,0x000000076eb00000)
//    from space 10752K, 0% used [0x000000076eb00000,0x000000076eb00000,0x000000076f580000)
//    to   space 10752K, 0% used [0x000000076f580000,0x000000076f580000,0x0000000770000000)
//    ParOldGen       total 175104K, used 451K [0x00000006c0000000, 0x00000006cab00000, 0x000000076ab00000)
//    object space 175104K, 0% used [0x00000006c0000000,0x00000006c0070df8,0x00000006cab00000)
//    Metaspace       used 3598K, capacity 4536K, committed 4864K, reserved 1056768K
//    class space    used 398K, capacity 428K, committed 512K, reserved 1048576K
}
