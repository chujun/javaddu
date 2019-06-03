package com.jun.chu.java.collection;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * @author: chujun
 * @date: 2019-06-03 17:20
 */
public class TestSet {
    @Test
    public void test() {
        Set<Integer> set = Sets.newHashSet(1, 2);
        //集合取交集
        set.retainAll(Sets.newHashSet());
        Assert.assertEquals(0, set.size());
    }
}
