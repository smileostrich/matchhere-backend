package com.mh.match.chat.service;


import com.mh.match.chat.dto.ChatMessageKafkaDto;
import com.mh.match.chat.dto.ChatMessageResponseDto;
import com.mh.match.chat.dto.request.ChatMessageRequestDto;
import com.mh.match.chat.dto.response.ChatRoomResponseDto;
import com.mh.match.chat.dto.response.MemberChatRoomResponseDto;
import com.mh.match.chat.repository.ChatMessageRepository;
import com.mh.match.chat.repository.ChatRoomRepository;
import com.mh.match.jwt.TokenProvider;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.util.SecurityUtil;
import com.mh.match.chat.entity.ChatMessage;
import com.mh.match.chat.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ChatService {
    @Autowired
    private KafkaSenderService kafkaSenderService;

    @Autowired
    private KafkaReceiverService kafkaReceiverService;

    private final TokenProvider tokenProvider;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    private static String BOOT_TOPIC = "kafka-chat";

    @Transactional
    public void sendMessage(ChatMessageRequestDto chatMessageRequestDto, String token, Long id) {
//        ConcurrentHashMap<String, String> concurrentHashMap = tokenProvider.getUserDataFromJwt(token);
//        String roomid = getRoomId(Long.parseLong(concurrentHashMap.get("userid")), other.getId());
//        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomid);
//        ChatMessage message;
//        if (chatRoom.isEmpty()) {
//            ChatRoom inner_chatroom = createChatRoom(roomid, user, other);
////            ChatRoom inner_chatroom = createChatRoom(roomid, Long.parseLong(concurrentHashMap.get("userid")), concurrentHashMap.get("nickname"), other.getId(), other.getNickname());
//            chatRoomRepository.save(inner_chatroom);
//            message = chatMessageRequestDto.toChatMessage(user, inner_chatroom);
//        } else {
//            message = chatMessageRequestDto.toChatMessage(user, chatRoom.get());
//        }
        ConcurrentHashMap<String, String> concurrentHashMap = tokenProvider.getUserDataFromJwt(token);
        Long userid = Long.parseLong(concurrentHashMap.get("userid"));
        Member user = memberRepository.findById(userid).orElseThrow(() -> new NullPointerException("잘못된 사용자 토큰입니다!"));
        Member other = memberRepository.findById(id).orElseThrow(() -> new NullPointerException("존재하지 않는 사용자입니다!"));
        String roomid = getRoomId(userid, other.getId());
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomid);
        ChatMessage message;

        if (chatRoom.isEmpty()) {
            ChatRoom inner_chatroom = createChatRoom(roomid, user, other);
//            ChatRoom inner_chatroom = createChatRoom(roomid, Long.parseLong(concurrentHashMap.get("userid")), concurrentHashMap.get("nickname"), other.getId(), other.getNickname());
            chatRoomRepository.save(inner_chatroom);
            message = chatMessageRequestDto.toChatMessage(user, inner_chatroom);
        } else {
            message = chatMessageRequestDto.toChatMessage(user, chatRoom.get());
        }
        chatMessageRepository.save(message);

        kafkaSenderService.send(BOOT_TOPIC, ChatMessageKafkaDto.of(message));
    }

    @Transactional
    public ChatRoom createChatRoom(String roomid, Member user, Member other) {
        return ChatRoom.builder()
                .id(roomid)
                .other(other)
                .user(user)
                .build();
    }

//    @Transactional
//    public ChatRoom createChatRoom(String roomid, long user_id, String user_nickname, Long other_id, String other_nickname) {
//        return ChatRoom.builder()
//                .id(roomid)
//                .user_id(user_id)
//                .user_nickname(user_nickname)
//                .other_id(other_id)
//                .other_nickname(other_nickname)
//                .build();
//    }

//    @Transactional
//    public ChatRoom createChatRoom(String roomid, Long userid, String nickname) {
//        return ChatRoom.builder()
//                .id(roomid)
//                .other_id()
//                .user_id(userid)
//                .build();
//    }

    @Transactional(readOnly = true)
    public Page<ChatMessageResponseDto> getHistory(String roomid, Pageable pageable) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("잘못된 토큰입니다."));
//        String roomid = getRoomId(member.getId(), id);
        ChatRoom chatRoom = chatRoomRepository.findById(roomid).orElseThrow(() -> new NullPointerException("존재하지 않는 채팅방입니다!"));
//        List<ChatMessageInterface> chatMessages = chatMessageRepository.findAllByRoom(chatRoom);
        Page<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoom(chatRoom, pageable);
        return chatMessages.map(m -> ChatMessageResponseDto.of(m));
//        Page<ChatMessageResponseDto> chatMessageResponseDtos = new Page<>();
//        for (ChatMessage chatMessage:chatMessages) {
//            chatMessageResponseDtos.add(ChatMessageResponseDto.of(chatMessage));
//        }
//        return chatMessageResponseDtos;
//        ChatMessagesResponseDto chatMessagesResponseDto = ChatMessagesResponseDto.of(chatMessages);
//        return chatMessagesResponseDto;
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getChattingRooms() throws Exception {
//        Long user_id = SecurityUtil.getCurrentMemberId();
//        if (memberRepository.existsById(user_id)) {
//            throw new Exception("잘못된 토큰입니다!");
//        }
        Member user = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("잘못된 토큰입니다."));
        List<ChatRoom> chatRoomsUser = chatRoomRepository.findAllByUser(user);
        List<ChatRoom> chatRoomsOther = chatRoomRepository.findAllByOther(user);
        List<ChatRoomResponseDto> chatRoomResponseDtos = new ArrayList<>();
        for (ChatRoom chatRoom: chatRoomsUser) {
            ChatMessage chatMessage = chatMessageRepository.findTopByChatRoomOrderBySentTimeDesc(chatRoom);
            if (chatMessage != null) {
                chatRoomResponseDtos.add(ChatRoomResponseDto.ofOther(chatRoom, chatMessage));
            }
        }
        for (ChatRoom chatRoom: chatRoomsOther) {
            ChatMessage chatMessage = chatMessageRepository.findTopByChatRoomOrderBySentTimeDesc(chatRoom);
            if (chatMessage != null) {
                chatRoomResponseDtos.add(ChatRoomResponseDto.ofUser(chatRoom, chatMessage));
            }
        }
        return chatRoomResponseDtos;
//        List<ChatRoomUserInterface> chatRooms1 = chatRoomRepository.findAllOthersByUser_id(user_id);
//        List<ChatRoomUserInterface> chatRooms2 = chatRoomRepository.findAllOthersByUser_id(user_id);
//        chatRooms1.addAll(chatRooms2);
//        ChatRoomsResponseDto chatRoomsResponseDto = ChatRoomsResponseDto.of(chatRooms1);
//        return chatRoomsResponseDto;

    }

    @Transactional
    public MemberChatRoomResponseDto getChatroomId(String email) {
        Member user = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("잘못된 사용자 토큰입니다!"));
        Member other = memberRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("존재하지 않는 사용자입니다!"));
        String roomid = getRoomId(user.getId(), other.getId());
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomid);
        if (chatRoom.isEmpty()) {
            ChatRoom inner_chatroom = createChatRoom(roomid, user, other);
            chatRoomRepository.save(inner_chatroom);
            return MemberChatRoomResponseDto.of(inner_chatroom, other);
        } else {
            return MemberChatRoomResponseDto.of(chatRoom.get(), other);
        }
    }

    private String getRoomId(Long myid, Long id) {
        String roomid;
        if (myid > id){
            roomid = Long.toString(id) + '-' + Long.toString(myid);
        } else {
            roomid = Long.toString(myid) + '-' + Long.toString(id);
        }
        return roomid;
    }
}
