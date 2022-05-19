package com.jun.chu.java.leetcode.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * leetcode尽量不用第三方jar包
 *
 * @author chujun
 * @date 2022/5/19
 */
public class OriginJDKUtil {
    public static <T> List<T> newCopy(List<T> src, int initNumber) {
        if (null == src || 0 == src.size()) {
            return new ArrayList<>();
        }
        List<T> destList = new ArrayList<>(initNumber);
        destList.addAll(src);
        return destList;
    }

    public static <T> Set<T> newCopy(Set<T> src) {
        if (null == src || 0 == src.size()) {
            return new HashSet<>();
        }
        Set<T> destSet = new HashSet<>(src.size());
        destSet.addAll(src);
        return destSet;
    }

    public static List<Integer> newCopyList(int[] src) {
        if (null == src || 0 == src.length) {
            return new ArrayList<>();
        }
        List<Integer> dest = new ArrayList<>(src.length);
        for (final int num : src) {
            dest.add(num);
        }
        return dest;
    }

    public static Set<Integer> newCopySet(int[] src) {
        if (null == src || 0 == src.length) {
            return new HashSet<>();
        }
        Set<Integer> dest = new HashSet<>(src.length);
        for (final int num : src) {
            dest.add(num);
        }
        return dest;
    }
}
