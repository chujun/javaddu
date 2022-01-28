package com.jun.chu.java.jvm.memeryError;

import java.util.HashSet;
import java.util.Set;

/**
 * jdk6:-vm 设置永久代大小 -XX:PermSize=10M -XX:MaxPermSize=10M
 * jdk7:常量池已经移入到java堆 -Xmx10M
 * jdk8:永久代已不存在，方法区是java堆的一部分 -XX:MetaspaceSize=10M -XX:MaxMetaspaceSize=10M(没有生效方法区还是不断增加)
 *
 * @author chujun
 * @date 2022/1/27
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        strIntern();
        Set<String> set = new HashSet();
        long i = 0;
        long count = 1;
        boolean out = true;
        while (true) {
            long i1 = i++;
            set.add(String.valueOf(i1).intern());
            count++;
            if (count > Integer.MAX_VALUE && out) {
                System.out.println(count);
                out = false;
            }
        }
    }

    /**
     * jdk7之后输出结果:
     * ------------------------------------
     * true
     * false
     * ------------------------------------
     * 前者很好理解，后者返回false是因为
     * str2.intern()是因为这个字符串在在家sun.misc.Version的launcher_name常量时就已经加载到常量池中，不符合"首次遇到"
     * 而str2是因为new出来的新对象"java"字符串
     * <p>
     * 而在jdk6输出结果
     * ------------------------------------
     * false
     * false
     * ------------------------------------
     * jdk6没有保证intern的语句规范，俗称有String类的intern方法有bug,
     * 因为jdk6实现有bug
     */
    private static void strIntern() {
        String str1 = new StringBuffer("11111").append(2).toString();
        System.out.println(str1.intern() == str1);

        String str2 = new StringBuffer("ja").append("va").toString();
        System.out.println(str2.intern() == str2);
    }
}
