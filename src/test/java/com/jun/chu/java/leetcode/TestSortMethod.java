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

    @Test
    public void testQuickSort() {
        testSort(TestSortMethod::quickSort);
    }

    @Test
    public void testHeapSort() {
        testSort(TestSortMethod::heapSort);
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
                result.add(left.remove(0));
            } else {
                result.add(right.remove(0));
            }
        }

        if (left.size() > 0) {
            result.addAll(left);
        }

        if (right.size() > 0) {
            result.addAll(right);
        }

        return result;
    }

    public static <T extends Comparable<T>> void quickSort(List<T> list) {
        quickSort(list, 0, list.size() - 1);
    }

    /**
     * 快速排序
     * 算法核心思想:把序列分成左右两个序列,其中左序列的元素都比右序列的元素小，分治法递归处理完所有子序列。
     */
    public static <T extends Comparable<T>> void quickSort(List<T> list, int left, int right) {
        int partitionIndex;
        if (left < right) {
            partitionIndex = partition(list, left, right);
            quickSort(list, left, partitionIndex - 1);
            quickSort(list, partitionIndex + 1, right);
        }
    }

    /**
     * 分区操作，比基准值小的在左边序列，比基准值大的在右边序列
     */
    private static <T extends Comparable<T>> int partition(List<T> list,
                                                           int left,
                                                           int right) {
        // 设定基准值（pivot）
        int pivot = left;
        int index = pivot + 1;
        for (int i = index; i <= right; i++) {
            if (greatThan(list.get(pivot), list.get(i))) {
                swap(list, i, index);
                index++;
            }
        }
        swap(list, pivot, index - 1);
        return index - 1;
    }

    /**
     * 算法核心思想:
     * 待排序序列构建一个初始堆(例如大顶堆),将堆顶元素和待排序序列最后位置的元素交换,然后再调整堆,然后再不断交换堆顶元素和待排序序列元素交换位置,直到排序完成。
     * <p>
     * 算法描述
     * 1. 将初始待排序关键字序列(R1,R2….Rn)构建成大顶堆，此堆为初始的无序区。
     * 2. 将堆顶元素R[1]与最后一个元素R[n]交换，此时得到新的无序区(R1,R2,……Rn-1)和新的有序区(Rn),且满足R[1,2…n-1]<=R[n]；
     * 3. 由于交换后新的堆顶R[1]可能违反堆的性质，因此需要对当前无序区(R1,R2,……Rn-1)调整为新堆，然后再次将R[1]与无序区最后一个元素交换，得到新的无序区(R1,R2….Rn-2)和新的有序区(Rn-1,Rn)。不断重复此过程直到有序区的元素个数为n-1，则整个排序过程完成。
     */
    public static <T extends Comparable<T>> void heapSort(List<T> list) {
        //1.构建大顶堆
        for (int i = list.size() / 2 - 1; i >= 0; i--) {
            //从第一个非叶子结点从下至上，从右至左调整结构
            adjustHeap(list, i, list.size());
        }
        //2.调整堆结构+交换堆顶元素与堆尾部元素
        for (int j = list.size() - 1; j > 0; j--) {
            swap(list, 0, j);//将堆顶元素与末尾元素进行交换
            //堆的长度逐步减少
            adjustHeap(list, 0, j);//重新对堆进行调整
        }

    }

    /**
     * 调整大顶堆（仅是调整过程，建立在大顶堆已构建的基础上）
     * 节点下标为i的话--->左节点下标:2i+1,右节点下标:2i+2
     */
    private static <T extends Comparable<T>> void adjustHeap(List<T> list,
                                                             int i,
                                                             int heapSize) {
        T temp = list.get(i);//先取出当前元素i
        //从i结点的左子结点开始，也就是2i+1处开始
        for (int k = i * 2 + 1; k < heapSize; k = k * 2 + 1) {
            //取左右子节点大的元素下标
            if (k + 1 < heapSize && greatThan(list.get(k + 1), list.get(k))) {
                //如果左子结点小于右子结点，k指向右子结点
                k++;
            }
            //如果子节点大于父节点，将子节点值赋给父节点（不用进行交换）
            if (greatThan(list.get(k), temp)) {
                list.set(i, list.get(k));
                i = k;
            } else {
                break;
            }
        }
        //将temp值放到最终的位置
        list.set(i, temp);
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
