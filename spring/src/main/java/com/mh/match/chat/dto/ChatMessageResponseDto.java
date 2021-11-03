package com.mh.match.chat.dto;


import com.mh.match.chat.entity.ChatMessage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageResponseDto {
    private String content;
    private Long sender_id;
    private String pic_uri;
    private String nickname;
    private LocalDateTime sentTime;

    public static ChatMessageResponseDto of(ChatMessage chatMessage) {
        return ChatMessageResponseDto.builder()
                .content(chatMessage.getContent())
                .nickname(chatMessage.getSender().getNickname())
                .pic_uri((chatMessage.getSender().getCover_pic() == null) ? null : chatMessage.getSender().getCover_pic().getDownload_uri())
                .sender_id(chatMessage.getSender().getId())
                .sentTime(chatMessage.getSentTime())
                .build();
    }

    @Builder
    public ChatMessageResponseDto(String content, Long sender_id, String pic_uri, String nickname, LocalDateTime sentTime) {
        this.content = content;
        this.sender_id = sender_id;
        this.pic_uri = pic_uri;
        this.nickname = nickname;
        this.sentTime = sentTime;
    }
}
