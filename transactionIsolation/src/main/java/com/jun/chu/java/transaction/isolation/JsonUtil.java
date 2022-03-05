package com.jun.chu.java.transaction.isolation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author chujun
 * @date 2022/3/5
 */
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String toJson(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
