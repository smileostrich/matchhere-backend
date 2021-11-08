package com.mh.match.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mh.match.chat.dto.ChatMessageKafkaDto;
import com.mh.match.chat.dto.ChatMessageResponseDto;
import com.mh.match.chat.entity.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Service
public class KafkaReceiverService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReceiverService.class);

    @Autowired
    private SimpMessagingTemplate template;

//    @KafkaListener( topics = "kafka-chat", groupId = "${kafka.group.id:${random.uuid}}")
    @KafkaListener(id = "main-listener", topics = "kafka-chat")
    public void receive(ChatMessageKafkaDto message) throws Exception {
//        LOGGER.info("message='{}'", message);
        HashMap<String, String> msg = new HashMap<>();
        msg.put("sentTime", message.getSentTime().format(DateTimeFormatter.ISO_DATE_TIME));
        msg.put("nickname", message.getNickname());
        msg.put("content", message.getContent());
        msg.put("pic_uri", message.getPic_uri());
        msg.put("sender_id", Long.toString(message.getSender_id()));
//        msg.put("sentTime", message.getSentTime().format(DateTimeFormatter.ISO_DATE_TIME));
//        msg.put("nickname", message.getSender().getNickname());
//        msg.put("content", message.getContent());
//        msg.put("pic_uri", (message.getSender().getCover_pic() == null) ? "" : message.getSender().getCover_pic().getDownload_uri());
//        msg.put("sender_id", Long.toString(message.getSender().getId()));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(msg);

//        this.template.convertAndSend("/room/" + roomId, json);
//        this.template.convertAndSend("/room/" + message.getChatRoom().getId(), json);
        this.template.convertAndSend("/room/" + message.getRoom_id(), json);
    }

//    @KafkaListener(id = "main-listener", topics = "kafka-chat")
//    public void receive(ChatMessage message) throws Exception {
//        LOGGER.info("message='{}'", message);
//        HashMap<String, String> msg = new HashMap<>();
//        msg.put("sent_time", message.getSent_time().format(DateTimeFormatter.ISO_DATE_TIME));
//        msg.put("nickname", message.getNickname());
//        msg.put("content", message.getContent());
//        msg.put("sender_id", message.getSender_id());
//
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(msg);
//
//        this.template.convertAndSend("/topic/public", json);
//    }
}
