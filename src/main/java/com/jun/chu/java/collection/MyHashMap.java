package com.jun.chu.java.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chujun on 2017/12/15.
 */
public class MyHashMap {
    public static void main(String[] args) {
        System.out.println(Float.isNaN(2));
        System.out.println(Float.isNaN(2.0f));
        System.out.println(Float.isNaN(0.0f / 0.0f));
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            map.put(i + "", i + "");
        }

        Map<String, String> map2 = new HashMap<>();
        for (int i = 0; i < 23; i++) {
            map2.put(i + "", i + "");
        }
        //测试resize()执行两次的条件,m.size+size>capacity
        map.putAll(map2);

        Map<String, String> copyedMap = (Map<String, String>) ((HashMap) map).clone();
    }
}
