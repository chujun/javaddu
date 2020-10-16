package com.jun.chu.java.leetcode.digit;

import java.util.stream.IntStream;

/**
 * @author chujun
 * @link https://leetcode-cn.com/problems/median-of-two-sorted-arrays/
 * @date 2020-10-14
 * v1:先来个时间复杂度为O(m+n)的,不考虑m,n正序情况
 * v2.不需要合并数组，用双指标表示，空间复杂度为O(1),中位数===>寻找第k小问题
 * -> m+n为奇数:中位数位置,一个:(m+n)/2 +1
 * -> m+n为偶数:中位数位置,两个:(m+n)/2  ,(m+n)/2 +1
 * v3.考虑二分查找，时间复杂度为O(log(m+n)),排除法,递归法
 * 参考[评论最多的第二种递归式解法](https://leetcode-cn.com/problems/median-of-two-sorted-arrays/solution/xiang-xi-tong-su-de-si-lu-fen-xi-duo-jie-fa-by-w-2/)
 * 1 | 2| 3| 4
 * 1 | 3| 5| 7
 * a.如果k=1，则是寻找第一小，那么很简单,直接比较首数组元素大小,谁小取谁
 * b.比较两个数组k/2-1 m[k/2-1]与n[k/2-1]大小(先不考虑数组越界情况))
 * 如果m[k/2-1]<n[k/2-1],那么m[k/2-1]至多是(k/2-1)+1+(k/2-1)=k-1小元素(额外加1包含m[k/2-1]本身，不包含n[n/2-1])，不可能是第k小元素,
 * 所以m[0,,,k/2-1]排除掉，不可能是第k小元素了
 * 同理如果m[k/2-1]>n[k/2-1],那么n[k/2-1]至多是(k/2-1)+1+(k/2-1)=k-1小元素(额外加1包含n[k/2-1]本身，不包含m[n/2-1])，不可能是第k小元素
 * 所以n[0,,,k/2-1]排除掉，不可能是第k小元素了
 * 如果m[k/2-1]=n[k/2-1],中位数取任意一个都可以，就归类到第一种情况里面吧
 * c.如果一个数组为空，那么也很简单，则直接去另一个数组的第k个元素即可下标为[k-1]元素
 * d.再来考虑越界的场景,如果其中一个数组越界，即 k/2-1>=m.length或者k/2-1>=n.length,那么则可以直接取另一个不越界数组的
 * TODO
 * 假如m数组越界,那么取n数组n[]
 */
public class MedianOfTwoSortedArraysFor4 {
    public static void main(String[] args) {
        int[] int1 = IntStream.rangeClosed(0, 10000).toArray();
        int[] int2 = IntStream.rangeClosed(0, 20000).toArray();
        double medianSortedArrays = findMedianSortedArrays(int1, int2);
        System.out.println(medianSortedArrays);
        System.out.println(findMedianSortedArrays(new int[]{1,2},new int[]{3,4}));
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length == 0 && nums2.length == 0) {
            return 0.0d;
        }
        int length = nums1.length + nums2.length;
        boolean flag = (length & 1) == 0;
        int k1 = length / 2 + 1;
        int k2 = length / 2;
        return flag ? (getKth(nums1, 0, nums1.length - 1, nums2, 0, nums2.length - 1, k1) + getKth(nums1, 0, nums1.length - 1, nums2, 0, nums2.length - 1, k2)) / 2.0
            : getKth(nums1, 0, nums1.length - 1, nums2, 0, nums2.length - 1, k1) / 1.0;
    }

    /**
     * k:表示第k个元素，从1开始
     */
    public static int getKth(int[] nums1, int start1, int end1, int[] nums2, int start2, int end2, int k) {
        //计算两个数组长度
        int length1 = end1 - start1 + 1;
        int length2 = end2 - start2 + 1;
        //保证nums1长度一定小于num2长度
        if (length1 > length2) return getKth(nums2, start2, end2, nums1, start1, end1, k);
        if (0 == length1) {
            return nums2[start2 + k - 1];
        }
        if (1 == k) {
            return Math.min(nums1[start1], nums2[start2]);
        }
        //Math.min(length1,k/2)处理数组越界的问题,这样至多只能取到最后一个元素
        int half = k / 2;
        int index1 = start1 + Math.min(length1, half) - 1;
        int index2 = start2 + Math.min(length2, half) - 1;
        if (nums1[index1] < nums2[index2]) {
            return getKth(nums1, index1 + 1, end1, nums2, start2, end2, k - (index1 - start1 + 1));
        } else {
            return getKth(nums1, start1, end1, nums2, index2 + 1, end2, k - (index2 - start2 + 1));
        }
    }
}
