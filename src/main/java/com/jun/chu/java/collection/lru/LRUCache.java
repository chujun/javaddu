package com.jun.chu.java.collection.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by chujun on 2018/11/14. 基于现有的 jdk api实现最近未使用算法
 * 不求自己纯手工从底层开始打造出自己的LRU，但是起码知道如何利用已有的jdk数据结构实现一个java版的LRU
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int cacheSize;

    public LRUCache(int cacheSize) {
        super((int) Math.ceil(cacheSize / 0.75), 0.75f, true);
        this.cacheSize = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > cacheSize;
    }

    public static void main(String[] args) {
        LRUCache<Integer, Integer> lruCache = new LRUCache<>(5);
        int size = 1;
        for (int i = 0; i < 7; i++) {
            lruCache.put(size, size);
            System.out.println("size:" + size + "," + lruCache);
            size++;

        }
    }


}

//        size:1,{1=1}
//        size:2,{1=1, 2=2}
//        size:3,{1=1, 2=2, 3=3}
//        size:4,{1=1, 2=2, 3=3, 4=4}
//        size:5,{1=1, 2=2, 3=3, 4=4, 5=5}
//        size:6,{2=2, 3=3, 4=4, 5=5, 6=6}
//        size:7,{3=3, 4=4, 5=5, 6=6, 7=7}
