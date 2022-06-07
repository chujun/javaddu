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
    public void testBubbleSort() {
        List<Integer> list = initList();
        bubbleSort(list);
        System.out.println(list.toString());
    }

    @Test
    public void testSelectionSort() {
        List<Integer> list = initList();
        selectionSort(list);
        System.out.println(list.toString());
    }

    /**
     * 冒泡排序
     * 算法核心思想:不断比较相邻元素,如果第一个元素比第二个元素大，就交换他们的位置
     */
    public static <T extends Comparable<T>> void bubbleSort(List<T> list) {
        if (OriginJDKUtil.isEmpty(list)) {
            return;
        }
        int len = list.size();
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                // 相邻元素两两对比
                if (greatThan(list.get(j), list.get(j + 1))) {
                    // 元素交换
                    swap(list, j, j + 1);
                }
            }
        }
    }

    /**
     * 选择排序
     * 算法核心思想：数组分为待排序区和有序区,每次从待排序区取出最大元素放到有序区队尾
     */
    public static <T extends Comparable<T>> void selectionSort(List<T> list) {
        int len = list.size();
        int minIndex;
        for (int i = 0; i < len - 1; i++) {
            minIndex = i;
            for (int j = i + 1; j < len; j++) {
                // 寻找最小的数
                if (greatThan(list.get(minIndex), list.get(j))) {
                    // 将最小数的索引保存
                    minIndex = j;
                }
            }
            swap(list, i, minIndex);
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
