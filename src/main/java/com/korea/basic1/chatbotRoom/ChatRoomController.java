package com.korea.basic1.chatbotRoom;

import com.korea.basic1.Question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/create")
    public String create(){
        ChatRoom chatRoom = this.chatRoomService.saveDefaultChatRoomNum();
        Integer id = chatRoom.getId();
        return "redirect:/chatbot/" + id;
    }

    @GetMapping("/delete/{id}")
    public String chatRoomDelete(@PathVariable("id") Integer id) {
        ChatRoom chatRoom = this.chatRoomService.getChatRoom(id);
        this.chatRoomService.delete(chatRoom);
        return "redirect:/chatbot";
    }
}
