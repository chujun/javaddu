package com.jun.chu.java.collection;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author chujun
 * @date 2020-07-09
 */
public class TestSort {
    @Test
    public void testEmptySort() {
        List<String> list = Lists.newArrayList();
        list.sort(Comparator.naturalOrder());

        String[] strs = new String[0];
        System.out.println(Arrays.toString(strs));
    }
}
