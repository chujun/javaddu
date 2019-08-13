package com.jun.chu.java.collection;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * @author chujun
 * @date 2019-07-28 18:55
 */
public class TestNoneMatch {
    @Test
    public void test() {
        List<Integer> list = Collections.emptyList();
        int a = 1;
        Assert.assertEquals(true, list.stream().noneMatch(item -> a == 1));
        Assert.assertEquals(true, list.stream().noneMatch(item -> a != 1));
        Integer i = 66560;
        System.out.println(1 & 2);
        System.out.println(i & 65536);
    }
}
