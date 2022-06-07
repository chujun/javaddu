package com.jun.chu.java.leetcode;

import com.google.common.collect.Lists;
import com.jun.chu.java.leetcode.tools.OriginJDKUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author chujun
 * @date 2022/6/7
 */
public class TestSortMethod {
    @Test
    public void testBubbleSort() {
        testSort(TestSortMethod::bubbleSort);
    }

    @Test
    public void testSelectionSort() {
        testSort(TestSortMethod::selectionSort);
    }

    @Test
    public void testInsertionSort() {
        testSort(TestSortMethod::insertionSort);
    }

    @Test
    public void testShellSort() {
        testSort(TestSortMethod::shellSort);
    }

    @Test
    public void testMergeSortUse() {
        testSort(TestSortMethod::mergeSortUse);
    }

    private <T extends Comparable<T>> void testSort(Consumer<List> consumer) {
        testSort(consumer, "[1, 3, 5, 5, 7, 8, 10, 23, 35, 64]", initList());
        testSort(consumer, "[3, 5, 5, 7, 8, 10, 10, 23, 35, 45, 54, 64]", initList2());
        testSort(consumer, "[1, 2, 3, 4, 5, 5, 6, 7, 8, 9, 10]", initList3());
        testSort(consumer, "[1, 2, 3, 4, 5, 5, 6, 7, 8, 9, 10]", initList4());
    }

    private <T extends Comparable<T>> void testSort(Consumer<List> consumer,
                                                    String excepted,
                                                    List<T> list) {
        consumer.accept(list);
        Assert.assertEquals(excepted, list.toString());
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
                    //将最小数的索引保存
                    minIndex = j;
                }
            }
            swap(list, i, minIndex);
        }
    }

    /**
     * 插入排序
     * 算法核心思想:构建有序序列,待排序元素从后往前与有序序列比较,插入到合适位置。
     */
    public static <T extends Comparable<T>> void insertionSort(List<T> list) {
        int len = list.size();
        int preIndex;
        //待排序元素
        T tobeSorted;
        for (int i = 1; i < len; i++) {
            preIndex = i - 1;
            tobeSorted = list.get(i);
            while (preIndex >= 0 && greatThan(list.get(preIndex), tobeSorted)) {
                //大的元素往有序序列后面移动
                list.set(preIndex + 1, list.get(preIndex));
                preIndex--;
            }
            //仍在原来位置上的就不需要调整位置了
            if (i != preIndex) {
                //在合适位置出入待排序元素
                list.set(preIndex + 1, tobeSorted);
            }
        }
    }

    /**
     * 希尔排序
     * 插入排序的改进版,又叫"缩小增量排序"
     */
    public static <T extends Comparable<T>> void shellSort(List<T> list) {
        //增量gap，并逐步缩小增量
        int size = list.size();
        for (int gap = size / 2; gap > 0; gap = gap / 2) {
            //从第gap个元素，逐个对其所在组进行直接插入排序操作
            for (int i = gap; i < size; i++) {
                int j = i;
                T current = list.get(i);
                while (j - gap >= 0 && greatThan(list.get(j - gap), current)) {
                    list.set(j, list.get(j - gap));
                    j = j - gap;
                }
                list.set(j, current);
            }
            System.out.println(gap);
        }
    }

    public static <T extends Comparable<T>> void mergeSortUse(List<T> list) {
        List<T> mergedList = mergeSort(list);
        for (int i = 0; i < list.size(); i++) {
            list.set(i, mergedList.get(i));
        }
    }

    /**
     * 归并排序
     * 算法核心:采用分治法,将已有序的子序列合并，得到完全有序的序列
     */
    public static <T extends Comparable<T>> List<T> mergeSort(List<T> list) {
        int len = list.size();
        if (len < 2) {
            return list;
        }
        int middle = len / 2;
        List<T> left = new ArrayList<>(list.subList(0, middle));
        List<T> right = new ArrayList<>(list.subList(middle, len));
        return mergeSort(mergeSort(left), mergeSort(right));
    }

    public static <T extends Comparable<T>> List<T> mergeSort(List<T> left, List<T> right) {
        List<T> result = new ArrayList<>();
        while (left.size() > 0 && right.size() > 0) {
            if (greatThan(right.get(0), left.get(0))) {
                result.add(right.remove(0));
            } else {
                result.add(left.get(0));
            }
        }

        while (left.size() > 0)
            result.addAll(left);

        while (right.size() > 0)
            result.addAll(right);

        return result;
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

    private static List<Integer> initList2() {
        return Lists.newArrayList(54, 23, 45, 5, 10, 7, 8, 10, 3, 5, 64, 35);
    }

    private static List<Integer> initList3() {
        return Lists.newArrayList(1, 2, 3, 4, 5, 5, 6, 7, 8, 9, 10);
    }

    private static List<Integer> initList4() {
        return Lists.newArrayList(10, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1);
    }
}
