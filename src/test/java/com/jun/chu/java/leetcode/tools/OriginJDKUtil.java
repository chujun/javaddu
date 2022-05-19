package com.jun.chu.java.leetcode.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * leetcode尽量不用第三方jar包
 *
 * @author chujun
 * @date 2022/5/19
 */
public class OriginJDKUtil {
    public static List<Integer> newCopy(List<Integer> src, int initNumber) {
        if (null == src || 0 == src.size()) {
            return new ArrayList<>();
        }
        List<Integer> destList = new ArrayList<>(initNumber);
        for (Integer i : src) {
            destList.add(i);
        }
        return destList;
    }

    public static List<Integer> newCopy(int[] src) {
        if (null == src || 0 == src.length) {
            return new ArrayList<>();
        }
        List<Integer> dest = new ArrayList<>(src.length);
        for (final int num : src) {
            dest.add(num);
        }
        return dest;
    }
}
