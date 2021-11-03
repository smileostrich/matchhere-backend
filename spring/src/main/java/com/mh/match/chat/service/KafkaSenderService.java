package com.mh.match.chat.service;


import com.mh.match.chat.entity.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSenderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSenderService.class);

    @Autowired
    private KafkaTemplate<String, ChatMessage> kafkaTemplate;

    public void send(String topic, ChatMessage data) {
        LOGGER.info("sending data='{}' to topic='{}'", data, topic);
        kafkaTemplate.send(topic, data);// send to react clients via websocket(STOMP)
    }
}
