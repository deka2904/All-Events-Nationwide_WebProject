package com.korea.basic1.chatbot;

import com.korea.basic1.chatbotRoom.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatmessageService {
    private final ChatmessageRepository chatmessageRepository;

    public List<Chatmessage> getList() {
        return this.chatmessageRepository.findAll();
    }

    public List<Chatmessage> getListByRoomId(ChatRoom chatRoom) {
        return chatmessageRepository.findByChatRoom(chatRoom);
    }
    public void create(String query, String answer){
        Chatmessage chatmessage = new Chatmessage();
        chatmessage.setQuery(query);
        chatmessage.setAnswer(answer);
        chatmessage.setCreateDate(LocalDateTime.now());
        this.chatmessageRepository.save(chatmessage);
    }
}
