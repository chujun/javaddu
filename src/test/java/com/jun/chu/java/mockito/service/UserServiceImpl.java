package com.jun.chu.java.mockito.service;

import com.jun.chu.java.mockito.domain.User;
import com.jun.chu.java.mockito.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chujun
 * @date 2020-10-23
 */
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findByName(final String name) {
        System.out.println("call method : name=" + name);
        return userRepository.findByName(name);
    }
}
