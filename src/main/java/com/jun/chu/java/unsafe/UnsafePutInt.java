package com.jun.chu.java.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * putInt，putIntVolatile，putOrderedInt三种方法比较
 * 反汇编比较三种方法的不同之处,暂时没下载成功hsdis-amd64
 *
 * 如何生成java源代码的汇编语言
 * java -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -Xcomp target.classes.com.jun.chu.java.unsafe.UnsafePutInt
 * Java HotSpot(TM) 64-Bit Server VM warning: PrintAssembly is enabled; turning on DebugNonSafepoints to gain additional output
 * Could not load hsdis-amd64.dylib; library not loadable; PrintAssembly is disabled
 * 错误: 找不到或无法加载主类 target.classes.com.jun.chu.java.unsafe.UnsafePutInt
 *
 * # jre安装hsdis-amd64.dylib
 * https://github.com/a10y/hsdis-macos
 *
 * 要顺利使用 Java 的 "-XX:+PrintAssembly" 参数，需要手动编译（其实下载也行）和安装 "hsdis-amd64.dylib" 到本地系统。
 * macOS 下如何编译 hsdis
 * https://www.imlc.me/macOS%20%E4%B8%8B%E5%A6%82%E4%BD%95%E7%BC%96%E8%AF%91%20hsdis
 * @author chujun
 * @date 2021/9/9
 */
public class UnsafePutInt {

    private int a;
    private int b;
    private int c;

    public int getA() {
        return a;
    }

    public void setA(final int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(final int b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(final int c) {
        this.c = c;
    }

    private static final Unsafe unsafe;
    private static volatile long aOffset;
    private static volatile long bOffset;
    private static volatile long cOffset;

    static {
        //基于反射机制获取Unsafe类
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
            aOffset = unsafe.objectFieldOffset(UnsafePutInt.class.getDeclaredField("a"));
            bOffset = unsafe.objectFieldOffset(UnsafePutInt.class.getDeclaredField("b"));
            cOffset = unsafe.objectFieldOffset(UnsafePutInt.class.getDeclaredField("c"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        UnsafePutInt unsafePutInt = new UnsafePutInt();
        unsafePutInt.setA(1);
        System.out.println("a=" + aOffset + ",b=" + bOffset + ",c=" + cOffset);
        System.out.println(unsafePutInt);
        unsafe.putInt(unsafePutInt, aOffset, 2);
        unsafe.putInt(unsafePutInt, bOffset, 10);
        unsafe.putInt(unsafePutInt, cOffset, 100);
        System.out.println(unsafePutInt);
        unsafe.putIntVolatile(unsafePutInt, aOffset, 4);
        System.out.println("putIntVolatile:" + unsafePutInt);
        unsafe.putOrderedInt(unsafePutInt, aOffset, 8);
        System.out.println("putOrderedInt:" + unsafePutInt);
    }
}
