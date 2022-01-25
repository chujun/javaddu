package com.jun.chu.java.jvm.memeryError;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟java堆OutOfMemory异常
 * -VM:
 * -Xmx20m -Xmn20m -XX:+HeapDumpOnOutOfMemoryError
 *
 * @author chujun
 * @date 2022/1/25
 */
public class JavaHeapOOM {
    static class OOMObject {

    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }
//    Java HotSpot(TM) 64-Bit Server VM warning: MaxNewSize (20480k) is equal to or greater than the entire heap (20480k).  A new max generation size of 19968k will be used.
//    java.lang.OutOfMemoryError: Java heap space
//    Dumping heap to java_pid72453.hprof ...
//    Heap dump file created [27973937 bytes in 0.094 secs]
//    Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
//    at java.util.Arrays.copyOf(Arrays.java:3210)
//    at java.util.Arrays.copyOf(Arrays.java:3181)
//    at java.util.ArrayList.grow(ArrayList.java:265)
//    at java.util.ArrayList.ensureExplicitCapacity(ArrayList.java:239)
//    at java.util.ArrayList.ensureCapacityInternal(ArrayList.java:231)
//    at java.util.ArrayList.add(ArrayList.java:462)
//    at com.jun.chu.java.jvm.memeryError.JavaHeapOOM.main(JavaHeapOOM.java:22)
}
