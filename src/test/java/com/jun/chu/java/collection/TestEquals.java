package com.jun.chu.java.collection;

import com.google.common.collect.Lists;
import com.jun.chu.java.collection.hash.OrderItemRevokeCompareFactor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

/**
 * @author jun.chu
 * @date 2019-01-09 10:09
 */
public class TestEquals {
    @Test
    public void testEquals() {
        List<OrderItemRevokeCompareFactor> list1 = Lists.newArrayList();
        list1.add(new OrderItemRevokeCompareFactor(1, 1));
        list1.add(new OrderItemRevokeCompareFactor(3, 3));
        list1.add(new OrderItemRevokeCompareFactor(2, 2));

        List<OrderItemRevokeCompareFactor> list2 = Lists.newArrayList();
        list2.add(new OrderItemRevokeCompareFactor(1, 1));
        list2.add(new OrderItemRevokeCompareFactor(2, 2));
        list2.add(new OrderItemRevokeCompareFactor(3, 3));
        Assert.assertNotEquals(list1, list2);
        list1.sort(Comparator.comparing(OrderItemRevokeCompareFactor::getSkuId));
        list2.sort(Comparator.comparing(OrderItemRevokeCompareFactor::getSkuId));
        Assert.assertEquals(list1, list2);
        System.out.println(1000*60*60*24);
    }
}
