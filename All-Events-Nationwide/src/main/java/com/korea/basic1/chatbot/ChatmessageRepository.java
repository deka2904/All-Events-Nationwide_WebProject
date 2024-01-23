package com.korea.basic1.chatbot;

import com.korea.basic1.chatbotRoom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatmessageRepository extends JpaRepository<Chatmessage, Integer> {
    List<Chatmessage> findByChatRoom(ChatRoom chatRoom);
}
