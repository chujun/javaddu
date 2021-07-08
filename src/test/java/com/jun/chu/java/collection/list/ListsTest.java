package com.jun.chu.java.collection.list;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.mapstruct.ap.shaded.freemarker.template.utility.CollectionUtils;

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

    @Test
    public void testRetain(){
        List<Integer> list1=Lists.newArrayList(1,2,3);
        List<Integer> list2 =Lists.newArrayList(2,3,4);
        List<Integer> list3=Lists.newArrayList(5);
        List<Integer> list4=Lists.newArrayList();
        System.out.println(list1.retainAll(list2));
        System.out.println(list1.retainAll(list3));
        System.out.println(list1.retainAll(list4));

    }
}
