package com.jun.chu.java.jvm.gc;

/**
 *
 * <p>
 * 当MinorGC发现Survivor区无法存储eden区的对象时,对象就直接放到老年代
 *
 * @author chujun
 * @date 2022/6/11
 */
public class JavaGcOldGenerationDemo {
    /*
    -verbose:gc
-Xmx200M
-Xms200M
-Xmn50M
-XX:+PrintGCDetails
-XX:TargetSurvivorRatio=60
-XX:+PrintTenuringDistribution
-XX:+PrintGCDateStamps
-XX:MaxTenuringThreshold=15
     */
    public static void main(String[] args) {
        byte[] allocation1, allocation2, allocation3, allocation4, allocation5;
        //38912*0.92/1024=34.96M,eden区34M够分配,35M不够分配
        allocation1 = new byte[34 * 1024 * 1024];
        System.out.println("------start Allocation Failure------");
        allocation2 = new byte[1024 * 1024];
        System.out.println("------end Allocation Failure------");
        allocation3 = new byte[1024 * 1024];
        allocation4 = new byte[1024 * 1024];
        allocation5 = new byte[1024 * 1024];
    }
}
/*
空main方法执行如下
Heap
 PSYoungGen      total 45056K, used 3113K [0x00000007bce00000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 38912K, 8% used [0x00000007bce00000,0x00000007bd10a638,0x00000007bf400000)
  from space 6144K, 0% used [0x00000007bfa00000,0x00000007bfa00000,0x00000007c0000000)
  to   space 6144K, 0% used [0x00000007bf400000,0x00000007bf400000,0x00000007bfa00000)
 ParOldGen       total 153600K, used 0K [0x00000007b3800000, 0x00000007bce00000, 0x00000007bce00000)
  object space 153600K, 0% used [0x00000007b3800000,0x00000007b3800000,0x00000007bce00000)
 Metaspace       used 3080K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 339K, capacity 388K, committed 512K, reserved 1048576K
 */

/*
//执行方法
//start Allocation Failure)
2022-06-11T23:15:07.523-0800: [GC (Allocation Failure)
Desired survivor size 6291456 bytes, new threshold 7 (max 15)
[PSYoungGen: 37929K->624K(45056K)] 37929K->35448K(198656K), 0.0214842 secs] [Times: user=0.05 sys=0.01, real=0.02 secs]
Heap
 PSYoungGen      total 45056K, used 5514K [0x00000007bce00000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 38912K, 12% used [0x00000007bce00000,0x00000007bd2c6a70,0x00000007bf400000)
  from space 6144K, 10% used [0x00000007bf400000,0x00000007bf49c010,0x00000007bfa00000)
  to   space 6144K, 0% used [0x00000007bfa00000,0x00000007bfa00000,0x00000007c0000000)
 ParOldGen       total 153600K, used 34824K [0x00000007b3800000, 0x00000007bce00000, 0x00000007bce00000)
  object space 153600K, 22% used [0x00000007b3800000,0x00000007b5a02010,0x00000007bce00000)
 Metaspace       used 3082K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 339K, capacity 388K, committed 512K, reserved 1048576K
 */
