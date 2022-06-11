package com.jun.chu.java.jvm.gc;

/**
 * 新生代晋升老年代的实时动态年龄阈值实验
 * 本实例用于java GC以后，新生代survivor区域的变化，以及晋升到老年代的时间和方式的测试代码。需要自行分步注释不需要的代码进行反复测试对比
 * <p>
 * 由于java的main函数以及其他基础服务也会占用一些eden空间，所以要提前空跑一次main函数，来看看这部分占用。
 * jdk8 mac系统 main函数空运行gc信息如下
 * Heap
 *  par new generation   total 46080K, used 3277K [0x00000007b3800000, 0x00000007b6a00000, 0x00000007b6a00000)
 *   eden space 40960K,   8% used [0x00000007b3800000, 0x00000007b3b33610, 0x00000007b6000000)
 *   from space 5120K,   0% used [0x00000007b6000000, 0x00000007b6000000, 0x00000007b6500000)
 *   to   space 5120K,   0% used [0x00000007b6500000, 0x00000007b6500000, 0x00000007b6a00000)
 *  concurrent mark-sweep generation total 153600K, used 0K [0x00000007b6a00000, 0x00000007c0000000, 0x00000007c0000000)
 *  Metaspace       used 3078K, capacity 4496K, committed 4864K, reserved 1056768K
 *   class space    used 339K, capacity 388K, committed 512K, reserved 1048576K
 * <p>
 * 自定义的代码中，我们使用堆内分配数组和栈内分配数组的方式来分别模拟不可被GC的和可被GC的资源。
 */
public class JavaGcDynamicTenuringThresholdTest {
    /*
    -verbose:gc
-Xmx200M
-Xms200M
-Xmn50M
-XX:+PrintGCDetails
-XX:TargetSurvivorRatio=60
-XX:+PrintTenuringDistribution
-XX:+PrintGCDateStamps
-XX:MaxTenuringThreshold=3
-XX:+UseConcMarkSweepGC
-XX:+UseParNewGC
     */
    public static void main(String[] args) throws InterruptedException {
        //空跑一次main函数来查看java服务本身占用的空间大小，我这里是占用了3M。eden区大小50*80%=40M,所以40-3=37，37M占满eden区大小,下面分配三个1M的数组和一个34M的垃圾数组。

        //8:1:1,Survivor区=50M*10%=5M
        // 为了达到TargetSurvivorRatio（期望占用的Survivor区域的大小）这个比例指定的值, 即5M*60%=3M(Desired survivor size)，
        // 这里用1M的数组的分配来达到Desired survivor size
        //说明: 5M为S区的From或To的大小，60%为TargetSurvivorRatio参数指定,可以更改参数获取不同的效果。
        byte[] byte1m_1 = new byte[1 * 1024 * 1024];
        byte[] byte1m_2 = new byte[1 * 1024 * 1024];
        byte[] byte1m_3 = new byte[1 * 1024 * 1024];

        //使用函数方式来申请空间，函数运行完毕以后，就会变成垃圾等待回收。此时应保证eden的区域占用达到100%。可以通过调整传入值来达到效果。
        makeGarbage(34);

        //再次申请一个数组，eden会发生内存分配失败,ParNew "GC (Allocation Failure)"字样,因为eden已经满了，所以这里会触发Minor GC
        byte[] byteArr = new byte[10 * 1024 * 1024];
        // 这次Minor Gc时, 三个1M的数组因为尚有引用，所以进入From区域（因为是第一次GC）age为1
        // 且由于From区已经占用达到了60%(-XX:TargetSurvivorRatio=60), 所以会重新计算对象晋升的age。
        // 计算方法见上文，计算出age：min(age, MaxTenuringThreshold) = 1，输出中会有Desired survivor size 3145728 bytes, new threshold 1 (max 3)字样
        //新的数组byteArr进入eden区域。

        //第二次发生内存分配失败,GC (Allocation Failure)
        //再次触发垃圾回收，证明三个1M的数组会因为其第二次回收后age为2，大于上一次计算出的new threshold 1，所以进入老年代。
        //而byteArr因为超过survivor的单个区域，直接进入了老年代。
        makeGarbage(34);
    }

    private static void makeGarbage(int size) {
        byte[] byteArrTemp = new byte[size * 1024 * 1024];
    }
}
/*
执行结果如下
2022-06-11T21:47:13.514-0800: [GC (Allocation Failure) 2022-06-11T21:47:13.514-0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 1 (max 3)
- age   1:    3685184 bytes,    3685184 total
: 40346K->3619K(46080K), 0.0130135 secs] 40346K->3619K(199680K), 0.0130725 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2022-06-11T21:47:13.529-0800: [GC (Allocation Failure) 2022-06-11T21:47:13.529-0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 3 (max 3)
: 13859K->0K(46080K), 0.0123505 secs] 13859K->13777K(199680K), 0.0124057 secs] [Times: user=0.02 sys=0.01, real=0.02 secs]
Heap
 par new generation   total 46080K, used 35225K [0x00000007b3800000, 0x00000007b6a00000, 0x00000007b6a00000)
  eden space 40960K,  86% used [0x00000007b3800000, 0x00000007b5a667e8, 0x00000007b6000000)
  from space 5120K,   0% used [0x00000007b6000000, 0x00000007b6000000, 0x00000007b6500000)
  to   space 5120K,   0% used [0x00000007b6500000, 0x00000007b6500000, 0x00000007b6a00000)
 concurrent mark-sweep generation total 153600K, used 13777K [0x00000007b6a00000, 0x00000007c0000000, 0x00000007c0000000)
 Metaspace       used 3080K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 339K, capacity 388K, committed 512K, reserved 1048576K
 */
