package com.jun.chu.java;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author chujun
 * @date 2022/11/10
 */
public class ListOperation {
    public static Integer sum(List<Integer> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return list.stream().mapToInt(item -> item).sum();
    }
}
