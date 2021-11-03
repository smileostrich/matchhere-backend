package com.mh.match.chat.dto;


import java.time.LocalDateTime;

public interface ChatMessageInterface {
    String getContent();
    String getSender_id();
    LocalDateTime getSent_time();
    String getNickname();

//    String getDbFile();
}
