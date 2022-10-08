package com.jun.chu.java.spring.boot.test.jpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 00065906
 * @date 2022/9/22
 * By default, data JPA tests are transactional and roll back at the end of each test
 * https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-jpa-test
 */
@DataJpaTest
class ExampleRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

//    @Autowired
//    private UserRepository repository;
//
//    @Test
//    void testExample() throws Exception {
//        this.entityManager.persist(new User("sboot", "1234"));
//        User user = this.repository.findByUsername("sboot");
//        assertThat(user.getUsername()).isEqualTo("sboot");
//        assertThat(user.getVin()).isEqualTo("1234");
//    }

}
