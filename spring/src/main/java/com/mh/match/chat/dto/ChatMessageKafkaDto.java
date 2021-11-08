package com.mh.match.chat.dto;


import com.mh.match.chat.entity.ChatMessage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatMessageKafkaDto {
    private String content;
    private Long sender_id;
    private String pic_uri;
    private String nickname;
    private LocalDateTime sentTime;
    private String room_id;

    public static ChatMessageKafkaDto of(ChatMessage chatMessage) {
        return ChatMessageKafkaDto.builder()
                .content(chatMessage.getContent())
                .nickname(chatMessage.getSender().getNickname())
                .pic_uri((chatMessage.getSender().getCover_pic() == null) ? "" : chatMessage.getSender().getCover_pic().getDownload_uri())
                .sender_id(chatMessage.getSender().getId())
                .sentTime(chatMessage.getSentTime())
                .room_id(chatMessage.getChatRoom().getId())
                .build();
    }

    @Builder
    public ChatMessageKafkaDto(String content, Long sender_id, String pic_uri, String nickname, LocalDateTime sentTime, String room_id) {
        this.content = content;
        this.sender_id = sender_id;
        this.pic_uri = pic_uri;
        this.nickname = nickname;
        this.sentTime = sentTime;
        this.room_id = room_id;
    }
}
