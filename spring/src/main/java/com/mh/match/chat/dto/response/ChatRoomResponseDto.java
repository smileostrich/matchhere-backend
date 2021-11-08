package com.mh.match.chat.dto.response;

import com.mh.match.chat.entity.ChatMessage;
import com.mh.match.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatRoomResponseDto {
    private String id;
    private Long user_id;
    private String user_nickname;
    private String pic_uri;
    private String content;
    private String email;
    private LocalDateTime sentTime;

    public static ChatRoomResponseDto ofUser(ChatRoom chatRoom, ChatMessage chatMessage) {
        return ChatRoomResponseDto.builder()
                .id(chatRoom.getId())
                .user_id(chatRoom.getUser().getId())
                .user_nickname(chatRoom.getUser().getNickname())
                .pic_uri((chatRoom.getUser().getCover_pic()==null) ? "" : chatRoom.getUser().getCover_pic().getDownload_uri())
                .content(chatMessage.getContent())
                .email(chatRoom.getUser().getEmail())
                .sentTime(chatMessage.getSentTime())
                .build();
    }

    public static ChatRoomResponseDto ofOther(ChatRoom chatRoom, ChatMessage chatMessage) {
        return ChatRoomResponseDto.builder()
                .id(chatRoom.getId())
                .user_id(chatRoom.getOther().getId())
                .user_nickname(chatRoom.getOther().getNickname())
                .pic_uri((chatRoom.getOther().getCover_pic()==null) ? "" : chatRoom.getOther().getCover_pic().getDownload_uri())
                .content(chatMessage.getContent())
                .email(chatRoom.getOther().getEmail())
                .sentTime(chatMessage.getSentTime())
                .build();
    }

    @Builder
    public ChatRoomResponseDto(String id, Long user_id, String user_nickname, String pic_uri, String content, String email, LocalDateTime sentTime) {
        this.id = id;
        this.user_id = user_id;
        this.user_nickname = user_nickname;
        this.pic_uri = pic_uri;
        this.content = content;
        this.email = email;
        this.sentTime = sentTime;
    }
}
