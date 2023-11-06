package com.korea.basic1.chatbotRoom;

import com.korea.basic1.DataNotFoundException;
import com.korea.basic1.Question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    public List<ChatRoom> getList(){
        return this.chatRoomRepository.findAll();
    }
}
