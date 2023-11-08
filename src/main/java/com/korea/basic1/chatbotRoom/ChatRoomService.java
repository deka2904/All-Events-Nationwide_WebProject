package com.korea.basic1.chatbotRoom;

import com.korea.basic1.Answer.Answer;
import com.korea.basic1.DataNotFoundException;
import com.korea.basic1.Question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    public List<ChatRoom> getList(){
        return this.chatRoomRepository.findAll();
    }

    public ChatRoom getChatRoom(Integer id) {
        Optional<ChatRoom> chatRoom = this.chatRoomRepository.findById(id);
        if (chatRoom.isPresent()) {
            return chatRoom.get();
        } else {
            throw new DataNotFoundException("chatRoom not found");
        }
    }

    public void modify(ChatRoom chatRoom, String RoomName) {
        chatRoom.setRoomName(RoomName);
        this.chatRoomRepository.save(chatRoom);
    }
    public ChatRoom saveDefaultChatRoomNum(){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName("new_chat");
        return this.chatRoomRepository.save(chatRoom);
    }

    public void delete(ChatRoom chatRoom) {
        this.chatRoomRepository.delete(chatRoom);
    }
}
