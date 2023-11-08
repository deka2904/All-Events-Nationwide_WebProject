package com.korea.basic1.chatbotRoom;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomForm {
    @NotEmpty(message = "채팅방 이름은 필수항목입니다.")
    private String RoomName;
}
