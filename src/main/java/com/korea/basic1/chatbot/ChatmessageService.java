package com.korea.basic1.chatbot;

import com.korea.basic1.chatbotRoom.ChatRoom;
import com.korea.basic1.chatbotRoom.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatmessageService {
    private final ChatmessageRepository chatmessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    public List<Chatmessage> getList() {
        return this.chatmessageRepository.findAll();
    }

    public List<Chatmessage> getListByRoomId(ChatRoom chatRoom) {
        return chatmessageRepository.findByChatRoom(chatRoom);
    }

    public void create(String query, String answer, Integer id){
        Chatmessage chatmessage = new Chatmessage();
        Optional<ChatRoom> chatRoomOptional = this.chatRoomRepository.findById(id);

        if (chatRoomOptional.isPresent()) {
            ChatRoom chatRoom = chatRoomOptional.get();
            chatmessage.setQuery(query);
            chatmessage.setAnswer(answer);
            chatmessage.setCreateDate(LocalDateTime.now());
            chatmessage.setChatRoom(chatRoom);
            this.chatmessageRepository.save(chatmessage);
        } else {
        }
    }
}
