package com.jun.chu.java.leetcode;

import com.google.common.collect.Lists;
import com.jun.chu.java.leetcode.tools.OriginJDKUtil;
import org.junit.Test;

import java.util.List;

/**
 * @author chujun
 * @date 2022/6/7
 */
public class TestSortMethod {
    @Test
    public void test() {
        List<Integer> list = initList();
        bubbleSort(list);
        System.out.println(list.toString());
    }

    /**
     * 冒泡排序
     */
    public static <T extends Comparable<T>> void bubbleSort(List<T> list) {
        if (OriginJDKUtil.isEmpty(list)) {
            return;
        }
        int len = list.size();
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (greatThan(list.get(j), list.get(j + 1))) {        // 相邻元素两两对比
                    swap(list, j, j + 1);
                }
            }
        }
    }

    private static <T> void swap(List<T> list, int index, int index2) {
        T tmp = list.get(index);
        list.set(index, list.get(index2));
        list.set(index2, tmp);
    }

    private static <T extends Comparable<T>> boolean greatThan(T a, T b) {
        return 0 < a.compareTo(b);
    }

    private static List<Integer> initList() {
        return Lists.newArrayList(1, 23, 5, 7, 8, 10, 3, 5, 64, 35);
    }
}
