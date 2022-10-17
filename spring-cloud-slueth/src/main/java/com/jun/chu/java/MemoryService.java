package com.jun.chu.java;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author 00065906
 * @date 2022/10/14
 */
@Service
public class MemoryService {
    private static Logger LOG = LoggerFactory.getLogger(MemoryService.class);

    public void save(final String topic, MqttMessage message) {
        LOG.info("topic:{},message {}", topic, message);
    }
}
