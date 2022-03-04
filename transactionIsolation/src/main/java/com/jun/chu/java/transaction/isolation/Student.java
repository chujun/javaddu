package com.jun.chu.java.transaction.isolation;

import lombok.Builder;
import lombok.Data;

/**
 * @author chujun
 * @date 2022/3/4
 */
@Data
@Builder
public class Student {
    private Long id;

    private String name;

    private Integer gender;

    private Long grade;
}
