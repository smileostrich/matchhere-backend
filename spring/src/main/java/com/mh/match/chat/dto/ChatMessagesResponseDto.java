package com.mh.match.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatMessagesResponseDto {
//    private List<ChatMessageInterface> chatList = new ArrayList<>();
    private List<ChatMessageResponseDto> chatList = new ArrayList<>();
//    public static ChatMessagesResponseDto of(List<ChatMessageInterface> chatMessageInterfaces) {
//        return ChatMessagesResponseDto.builder()
//                .chatMessageInterfaces(chatMessageInterfaces)
//                .build();
//    }

    public static ChatMessagesResponseDto of(List<ChatMessageResponseDto> chatMessages) {
        return ChatMessagesResponseDto.builder()
                .chatMessageInterfaces(chatMessages)
                .build();
    }

    @Builder
    public ChatMessagesResponseDto(List<ChatMessageResponseDto> chatMessageInterfaces) {
        this.chatList = chatMessageInterfaces;
    }
}
