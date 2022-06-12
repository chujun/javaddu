package com.jun.chu.java.jvm.gc;

/**
 * [4.1 调整最大堆内存和最小堆内存](https://javaguide.cn/java/jvm/jvm-intro.html#_4-1-%E8%B0%83%E6%95%B4%E6%9C%80%E5%A4%A7%E5%A0%86%E5%86%85%E5%AD%98%E5%92%8C%E6%9C%80%E5%B0%8F%E5%A0%86%E5%86%85%E5%AD%98)
 * @author chujun
 * @date 2022/6/12
 */
public class JvmRuntime {
    public static void main(String[] args) {
        //-Xmx20m -Xms5m -XX:+PrintGCDetails
        byte[] b = new byte[10 * 1024 * 1024];
        System.out.println("分配了1M空间给数组");
        System.gc();
        System.out.println("Xmx=" + Runtime.getRuntime().maxMemory() / 1024.0 / 1024 + "M");    //系统的最大空间
        System.out.println("free mem=" + Runtime.getRuntime().freeMemory() / 1024.0 / 1024 + "M");  //系统的空闲空间
        System.out.println("total mem=" + Runtime.getRuntime().totalMemory() / 1024.0 / 1024 + "M");  //当前可用的总空间
    }
    /*
     [GC (Allocation Failure) [PSYoungGen: 1024K->496K(1536K)] 1024K->504K(5632K), 0.0007511 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     Xmx=18.0M
     free mem=4.123863220214844M
     total mem=5.5M
     Heap
     PSYoungGen      total 1536K, used 1422K [0x00000007bf980000, 0x00000007bfb80000, 0x00000007c0000000)
     eden space 1024K, 90% used [0x00000007bf980000,0x00000007bfa677f8,0x00000007bfa80000)
     from space 512K, 96% used [0x00000007bfa80000,0x00000007bfafc010,0x00000007bfb00000)
     to   space 512K, 0% used [0x00000007bfb00000,0x00000007bfb00000,0x00000007bfb80000)
     ParOldGen       total 4096K, used 8K [0x00000007bec00000, 0x00000007bf000000, 0x00000007bf980000)
     object space 4096K, 0% used [0x00000007bec00000,0x00000007bec02000,0x00000007bf000000)
     Metaspace       used 3131K, capacity 4496K, committed 4864K, reserved 1056768K
     class space    used 344K, capacity 388K, committed 512K, reserved 1048576K
     */
}
