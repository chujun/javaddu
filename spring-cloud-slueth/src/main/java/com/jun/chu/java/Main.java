package com.jun.chu.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author ${USER}
 * @date ${DATE}
 */
@SpringBootApplication
@RestController
public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);
    @Autowired
    private Executor executor;

    @RequestMapping("/")
    public String home() {
        log.info("Handling home");
        privateMethod("s");
        executor.execute(()->privateMethod("thread s"));
        executor.execute(()->privateMethod("thread s2"));
        return "Hello World";
    }

    private void privateMethod(final String s) {
        log.info("private {}",s);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }



    @Configuration
    @EnableAutoConfiguration
    @EnableAsync
    // add the infrastructure role to ensure that the bean gets auto-proxied
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    /**
     * spring-cloud-slueth对异步线程的支持
     * https://docs.spring.io/spring-cloud-sleuth/docs/2.2.8.RELEASE/reference/html/#asynchronous-communication
     */
    static class CustomExecutorConfig extends AsyncConfigurerSupport {

        @Autowired
        BeanFactory beanFactory;

        @Override
        @Bean
        public Executor getAsyncExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            // CUSTOMIZE HERE
            executor.setCorePoolSize(7);
            executor.setMaxPoolSize(42);
            executor.setQueueCapacity(11);
            executor.setThreadNamePrefix("MyExecutor-");
            // DON'T FORGET TO INITIALIZE
            executor.initialize();
            return new LazyTraceExecutor(this.beanFactory, executor);
        }

    }

}