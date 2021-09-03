package com.jun.chu.java.memory;

import com.javamex.classmexer.MemoryUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * or compiling: include classmexer.jar in your project, or make sure you include it in the classpath if you are compiling from the command line;
 * for running: add -javaagent:classmexer.jar to the java command used to start your application, making sure that a copy of the jar is in the working directory (the directory from which you start the application— not necessarily the same as the classpath).
 * 在idea编译时Open Module Settings->Modules->javaddu->Dependencies->
 * 添加classmexer.jar进来
 * <p>
 * 在idea运行时的具体操作就是:
 * 当前工作路径：/Users/chujun/my/project/javaddu
 * 将classmexer.jar到根目录下，直接运行即可
 *
 * @author chujun
 * @date 2021/9/3
 * @see <a href="https://www.javamex.com/classmexer/">https://www.javamex.com/classmexer/</a>
 * @see <a href="https://www.javamex.com/tutorials/memory/object_memory_usage.shtml>https://www.javamex.com/tutorials/memory/object_memory_usage.shtml</a>
 */
public class MemoryUsage {
    public static void main(String[] args) {
        long noBytes = MemoryUtil.memoryUsageOf(new Object());
        System.out.println(noBytes);
        System.out.println(MemoryUtil.memoryUsageOf(true));
        System.out.println("long:" + MemoryUtil.memoryUsageOf(1L));
        System.out.println(MemoryUtil.memoryUsageOf(1));
        System.out.println(MemoryUtil.memoryUsageOf('1'));
        System.out.println("str:" + MemoryUtil.memoryUsageOf("1"));
        System.out.println(MemoryUtil.memoryUsageOf((short) 1));
        System.out.println(MemoryUtil.memoryUsageOf((byte) 1));

        System.out.println("deepMemory:" + MemoryUtil.deepMemoryUsageOf(new Object()));
        IntegerObject integerObject = new IntegerObject();
        integerObject.setA(1);

        System.out.println(MemoryUtil.memoryUsageOf(integerObject));
        System.out.println(MemoryUtil.deepMemoryUsageOf(new Integer(1)));
        System.out.println(MemoryUtil.deepMemoryUsageOf(integerObject));

        IntObject intObject=new IntObject();
        System.out.println("裸一个int的object:"+MemoryUtil.deepMemoryUsageOf(intObject));
    }


    @Data
    @NoArgsConstructor
    public static class IntegerObject {
        private Integer a;
    }

    @Data
    @NoArgsConstructor
    public static class IntObject {
        private int a;
    }
}
