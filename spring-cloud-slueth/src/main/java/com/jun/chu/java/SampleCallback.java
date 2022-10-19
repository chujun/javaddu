package com.jun.chu.java;

import brave.ScopedSpan;
import brave.Span;
import brave.Tracer;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleCallback implements MqttCallback {
    private static Logger LOG = LoggerFactory.getLogger(SampleCallback.class);
    @Autowired
    private Tracer tracer;
    @Autowired
    private MemoryService memoryService;

    public void connectionLost(Throwable cause) {
        LOG.error("connection lost：" + cause.getMessage(), cause);
    }

    public void messageArrived(String topic, MqttMessage message) {
        ScopedSpan span = tracer.startScopedSpan("messageArrived");
        try {
            LOG.info("Received message: \n  topic：" + topic + "\n  Qos：" + message.getQos() + "\n  payload：" + new String(message.getPayload()));
            memoryService.save(topic, message);
        } finally {
            span.finish();
        }


    }

    public void deliveryComplete(IMqttDeliveryToken token) {

        LOG.info("deliveryComplete");
    }


}
