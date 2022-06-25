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

    @Test
    public void test() {
        int[] nums = new int[]{1, 2, 3, 3, 4, 6, 7, 3, 6};
        ArrayRemoveElement arrayRemoveElement = new ArrayRemoveElement();
        Assert.assertEquals(6, arrayRemoveElement.removeElement(nums, 3));
        System.out.println(OriginJDKUtil.newCopyList(nums));
    }
}
