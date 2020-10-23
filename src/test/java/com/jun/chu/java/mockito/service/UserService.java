package com.jun.chu.java.mockito.service;

import com.jun.chu.java.mockito.domain.User;

/**
 * @author chujun
 * @date 2020-10-23
 */
public interface UserService {
    User findByName(String name);
}
