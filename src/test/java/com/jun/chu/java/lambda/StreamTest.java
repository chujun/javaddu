package com.jun.chu.java.lambda;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jun.chu
 * @date 2019-01-14 14:22
 */
public class StreamTest {
    @Test
    public void testConcat() {
        List<Integer> first = Collections.singletonList(1);
        List<Integer> two = Lists.newArrayList(2, 3, 4, 5);
        List<Integer> result = Stream.concat(first.stream(), two.stream()).collect(Collectors.toList());
        System.out.println(result);
        Assert.assertEquals("[1, 2, 3, 4, 5]", result.toString());
        first = Lists.newArrayList(1,11);
        result = Stream.concat(first.stream(), two.stream()).collect(Collectors.toList());
        System.out.println(result);
        Assert.assertEquals("[1, 11, 2, 3, 4, 5]", result.toString());
    }

    @Test
    public void testEmptyListCollect() {
        List<Integer> list = Lists.newArrayList();
        Assert.assertEquals(0, list.stream().collect(Collectors.toList()).size());
    }
}
