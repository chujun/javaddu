package com.jun.chu.java.mockito.repository;

import com.google.common.collect.Lists;
import com.jun.chu.java.mockito.domain.User;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author chujun
 * @date 2020-10-23
 */
public class UserRepositoryImpl implements UserRepository {
    @Override
    public User findByName(final String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return getDataSource().stream().filter(item -> item.getName().equals(name))
            .findFirst().orElse(null);
    }

    private List<User> getDataSource() {
        return Lists.newArrayList(new User("a", "12"),
            new User("b", "123"));
    }
}
