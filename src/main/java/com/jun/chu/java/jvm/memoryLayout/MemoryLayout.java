package com.jun.chu.java.jvm.memoryLayout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author chujun
 * @date 2022/1/24
 */
public class MemoryLayout {
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

    public static void main(String[] args){

        Field[] declaredFields = NoChild.class.getDeclaredFields();
        printFeild(declaredFields);

        printFeild(NoChild2.class.getDeclaredFields());

        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseClass(NoChild.class).toPrintable());
        System.out.println(ClassLayout.parseClass(NoChild2.class).toPrintable());
        //jdk8 默认值:-XX:FieldsAllocationStyle=1 -XX:+CompactFields -XX:+UseCompressedOops -XX:+UseCompressedClassPointers

        //-XX:+PrintFlagsFinal -XX:FieldsAllocationStyle=1 -XX:-UseCompressedOops -XX:-UseCompressedClassPointers -XX:+CompactFields
        //com.jun.chu.java.jvm.memoryLayout.NoChild field value 						offset is 24
        //com.jun.chu.java.jvm.memoryLayout.NoChild field b 						offset is 20
        //com.jun.chu.java.jvm.memoryLayout.NoChild field i 						offset is 16

        //TODO:cj 无法解释为什么value的偏移量是24，而非20+1或者20+2，正好8*3
        //-XX:+PrintFlagsFinal -XX:FieldsAllocationStyle=1 -XX:-UseCompressedOops -XX:-UseCompressedClassPointers -XX:-CompactFields
        //com.jun.chu.java.jvm.memoryLayout.NoChild field value 						offset is 24
        //com.jun.chu.java.jvm.memoryLayout.NoChild field b 						offset is 20
        //com.jun.chu.java.jvm.memoryLayout.NoChild field i 						offset is 16

        //-XX:+PrintFlagsFinal -XX:FieldsAllocationStyle=1 -XX:+UseCompressedOops -XX:+UseCompressedClassPointers -XX:+CompactFields
        //com.jun.chu.java.jvm.memoryLayout.NoChild field value 						offset is 20
        //com.jun.chu.java.jvm.memoryLayout.NoChild field b 						offset is 16
        //com.jun.chu.java.jvm.memoryLayout.NoChild field i 						offset is 12

        //-XX:+PrintFlagsFinal -XX:FieldsAllocationStyle=0 -XX:-UseCompressedOops -XX:-UseCompressedClassPointers -XX:+CompactFields
        //com.jun.chu.java.jvm.memoryLayout.NoChild field value 						offset is 16
        //com.jun.chu.java.jvm.memoryLayout.NoChild field b 						offset is 28
        //com.jun.chu.java.jvm.memoryLayout.NoChild field i 						offset is 24
    }

    private static void printFeild(final Field[] declaredFields) {
        for (Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers())) {
                long offset = UNSAFE.staticFieldOffset(field);
                System.out.println(field.getDeclaringClass().getName() + " static field " + field.getName() + 					" offset is " + offset);
            } else {
                long offset = UNSAFE.objectFieldOffset(field);
                System.out.println(field.getDeclaringClass().getName() + " field " + field.getName() + " 						offset is " + offset);
            }
        }
    }
}
