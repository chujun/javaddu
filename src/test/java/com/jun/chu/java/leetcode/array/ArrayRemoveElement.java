package com.jun.chu.java.leetcode.array;

import com.jun.chu.java.leetcode.tools.OriginJDKUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * [leetcode 27. Array Remove Element](https://leetcode.cn/problems/remove-element/)
 *
 * @author chujun
 * @date 2022/6/25
 */
public class ArrayRemoveElement {
    public int removeElement(int[] nums, int val) {
        if (null == nums || 0 == nums.length) {
            return 0;
        }
        int resultLength = nums.length;
        int index = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == val) {
                resultLength--;
            } else {
                nums[index] = nums[i];
                index++;
            }
        }
        return resultLength;
    }

    /**
     * 双指针法
     * 思路:通过一个快指针和慢指针在一个for循环下完成两个for循环的工作。
     * 快指针：寻找新数组的元素 ，新数组就是不含有目标元素的数组
     * 慢指针：指向更新新数组下标的位置
     * 时间复杂度:On
     * 空间复杂度:O1
     */
    public int removeElementV2(int[] nums, int val) {
        if (null == nums || 0 == nums.length) {
            return 0;
        }
        //更新数组的待更新下标位置
        int slowIndex = 0;
        for (int fastIndex = 0; fastIndex < nums.length; fastIndex++) {
            if (nums[fastIndex] != val) {
                nums[slowIndex++] = nums[fastIndex];
            }
        }
        //slowIndex恰好等于新数组长度
        return slowIndex;
    }

    @Test
    public void test() {
        int[] nums = new int[]{1, 2, 3, 3, 4, 6, 7, 3, 6};
        ArrayRemoveElement arrayRemoveElement = new ArrayRemoveElement();
        Assert.assertEquals(6, arrayRemoveElement.removeElement(nums, 3));
        System.out.println(OriginJDKUtil.newCopyList(nums));
    }

    @Test
    public void testV2() {
        int[] nums = new int[]{1, 2, 3, 3, 4, 6, 7, 3, 6};
        ArrayRemoveElement arrayRemoveElement = new ArrayRemoveElement();
        Assert.assertEquals(6, arrayRemoveElement.removeElementV2(nums, 3));
        System.out.println(OriginJDKUtil.newCopyList(nums));
    }
}
