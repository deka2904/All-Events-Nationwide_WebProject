package com.korea.basic1.chatbotRoom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    List<ChatRoom> findBySiteUserId(Integer userId);
}
