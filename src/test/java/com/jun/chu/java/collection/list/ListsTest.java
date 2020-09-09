package com.jun.chu.java.collection.list;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chujun
 * @date 2020-07-22
 */
public class ListsTest {
    @Test
    public void test() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<List<Integer>> partition = Lists.partition(list, 3);
        System.out.println(partition);

        List<Integer> result = partition.stream().map(item -> {
            System.out.println(item);
            return item.size();
        }).collect(Collectors.toList());
        System.out.println(result);
    }
}
