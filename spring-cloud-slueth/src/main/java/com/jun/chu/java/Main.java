package com.jun.chu.java;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.Tracer;
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

/**
 * @author ${USER}
 * @date ${DATE}
 */
@SpringBootApplication
@RestController
public class Main {

    private static Logger LOG = LoggerFactory.getLogger(Main.class);
    @Autowired
    private Executor executor;

    @RequestMapping("/")
    public String home() {
        LOG.info("Handling home");
        privateMethod("s");
        executor.execute(() -> privateMethod("thread s"));
        executor.execute(() -> privateMethod("thread s2"));
        return "Hello World";
    }

    private void privateMethod(final String s) {
        LOG.info("private {}", s);
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

    @Configuration
    static class MqttClientConfiguration {
        private static Logger LOG = LoggerFactory.getLogger(MqttClientConfiguration.class);

        @Autowired
        private SampleCallback sampleCallback;
        @Bean
        public MqttClient createMqttClient() {
            String topic = "test/topic";
            int qos = 2;
            String broker = "tcp://192.168.142.141:1883";
            String clientId = MqttClient.generateClientId();
            MemoryPersistence persistence = new MemoryPersistence();
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("emqx_user");
            connOpts.setPassword("emqx_password".toCharArray());
            try {
                MqttClient client = new MqttClient(broker, clientId, persistence);
                client.setCallback(sampleCallback);

                LOG.info("Connecting to broker: " + broker);
                client.connect(connOpts);
                LOG.info("Connected to broker: " + broker);
                client.subscribe(topic, qos);
                LOG.info("Subscribed to topic: " + topic);
                return client;
            } catch (MqttException me) {
                LOG.error("MqttException cause", me);
                throw new RuntimeException(me);
            }
        }
    }

}