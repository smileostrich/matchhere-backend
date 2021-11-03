package com.mh.match.chat.dto.response;


import com.mh.match.chat.dto.ChatRoomUserInterface;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRoomsResponseDto {
    List<ChatRoomUserInterface> roomList = new ArrayList<>();

    public static ChatRoomsResponseDto of(List<ChatRoomUserInterface> chatRoomUserInterfaces) {
        return ChatRoomsResponseDto.builder()
                .roomList(chatRoomUserInterfaces)
                .build();
    }

    @Builder
    public ChatRoomsResponseDto(List<ChatRoomUserInterface> roomList) {
        this.roomList = roomList;
    }
}
