package com.jun.chu.java.memory;

import com.javamex.classmexer.MemoryUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * VM Options:
 * -javaagent:classmexer.jar -XX:-RestrictContended
 * <p>
 *
 * @author chujun
 * @date 2021/9/3
 * @see <a href="https://www.jianshu.com/p/c3c108c3dcfd">https://www.jianshu.com/p/c3c108c3dcfd</a>
 */
public class ContendedMemoryUsage {


    public static void main(String[] args) throws NoSuchFieldException {
        System.out.println("offset-a: " + Contended1.UNSAFE.objectFieldOffset(Contended1.class.getDeclaredField("a")));
        System.out.println("offset-b: " + Contended1.UNSAFE.objectFieldOffset(Contended1.class.getDeclaredField("b")));
        System.out.println("offset-c: " + Contended1.UNSAFE.objectFieldOffset(Contended1.class.getDeclaredField("c")));
        System.out.println("offset-d: " + Contended1.UNSAFE.objectFieldOffset(Contended1.class.getDeclaredField("d")));

        Contended1 contendedTest = new Contended1();

        // 打印对象的shallow size
        System.out.println("Shallow Size: " + MemoryUtil.memoryUsageOf(contendedTest) + " bytes");
        // 打印对象的 retained size
        System.out.println("Retained Size: " + MemoryUtil.deepMemoryUsageOf(contendedTest) + " bytes");


        System.out.println("offset-a: " + Contended2.UNSAFE.objectFieldOffset(Contended2.class.getDeclaredField("a")));
        System.out.println("offset-b: " + Contended2.UNSAFE.objectFieldOffset(Contended2.class.getDeclaredField("b")));
        System.out.println("offset-c: " + Contended2.UNSAFE.objectFieldOffset(Contended2.class.getDeclaredField("c")));
        System.out.println("offset-d: " + Contended2.UNSAFE.objectFieldOffset(Contended2.class.getDeclaredField("d")));

        Contended1 contendedTest2 = new Contended1();

        // 打印对象的shallow size
        System.out.println("Shallow Size: " + MemoryUtil.memoryUsageOf(contendedTest2) + " bytes");
        // 打印对象的 retained size
        System.out.println("Retained Size: " + MemoryUtil.deepMemoryUsageOf(contendedTest2) + " bytes");
    }

    public static class Contended1{
        byte a;
        @sun.misc.Contended("a")
        long b;
        @sun.misc.Contended("a")
        long c;
        int d;

        protected static Unsafe UNSAFE;

        static {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                UNSAFE = (Unsafe) f.get(null);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Contended2{
        byte a;
        @sun.misc.Contended("a")
        long b;
        @sun.misc.Contended("b")
        long c;
        int d;

        protected static Unsafe UNSAFE;

        static {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                UNSAFE = (Unsafe) f.get(null);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
