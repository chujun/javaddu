package com.jun.chu.java.collection.map;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chujun
 * @date 2019-07-25 15:30
 */
public class ConcurrentHashMapDeadLock {
    @Test
    public void test() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        map.computeIfAbsent("a", key -> {
            return 1;
        });
        System.out.println(map);
    }

    @Test
    public void testDeadLock() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        map.computeIfAbsent("a", key -> {
            Integer value = 1;
            System.out.println("开始put");
            map.put(key, value);
            System.out.println("这儿永远不会执行");
            return value;
        });
        System.out.println(map);
    }
}
