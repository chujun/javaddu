package com.jun.chu.java.lambda;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author jun.chu
 * @date 2019-03-09 16:29
 */
public class MapReduceTest {
    @Test
    public void testReduce() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4);
        List<Integer> reduceList = list.stream().map(item -> generate(item)).reduce(Lists.newArrayList(), (a, b) -> {
            a.addAll(b);
            return a;
        });
        System.out.println(reduceList);
        Assert.assertEquals("[0, 0, 1, 0, 1, 2, 0, 1, 2, 3]", reduceList.toString());
    }

    public List<Integer> generate(int size) {
        List<Integer> result = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            result.add(i);
        }
        return result;
    }
}
