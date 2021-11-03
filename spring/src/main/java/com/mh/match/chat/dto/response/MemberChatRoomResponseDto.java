package com.mh.match.chat.dto.response;


import com.mh.match.chat.entity.ChatRoom;
import com.mh.match.member.entity.Member;
import lombok.Builder;
import lombok.Data;

@Data
public class MemberChatRoomResponseDto {
    private String room_id;
    private Long user_id;
    private String user_nickname;
    private String pic_uri;

    public static MemberChatRoomResponseDto of(ChatRoom chatRoom, Member member) {
        return MemberChatRoomResponseDto.builder()
                .room_id(chatRoom.getId())
                .user_id(member.getId())
                .user_nickname(member.getNickname())
                .pic_uri((member.getCover_pic()==null) ? null: member.getCover_pic().getDownload_uri())
                .build();
    }

    @Builder
    public MemberChatRoomResponseDto(String room_id, Long user_id, String user_nickname, String pic_uri) {
        this.room_id = room_id;
        this.user_id = user_id;
        this.user_nickname = user_nickname;
        this.pic_uri = pic_uri;
    }

}
