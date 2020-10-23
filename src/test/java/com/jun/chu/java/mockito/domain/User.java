package com.jun.chu.java.mockito.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chujun
 * @date 2020-10-23
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String name;

    private String password;
}
