package com.jun.chu.java.mockito.repository;

import com.jun.chu.java.mockito.domain.User;

/**
 * @author chujun
 * @date 2020-10-23
 */
public interface UserRepository {
    User findByName(final String name);
}
