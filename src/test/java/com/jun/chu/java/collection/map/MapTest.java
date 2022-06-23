package com.jun.chu.java.collection.map;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chujun
 * @date 2022/6/23
 */
public class MapTest {
    @Test
    public void merge() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 1);
        map.merge(2, 1, Integer::sum);
        map.merge(1, 2, Integer::sum);
        Assert.assertEquals(3, (int) map.get(1));
        Assert.assertEquals(1, (int) map.get(2));
    }
}
