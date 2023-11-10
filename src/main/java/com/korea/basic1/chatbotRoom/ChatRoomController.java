package com.korea.basic1.chatbotRoom;

import com.korea.basic1.Answer.Answer;
import com.korea.basic1.Answer.AnswerForm;
import com.korea.basic1.DataNotFoundException;
import com.korea.basic1.Question.Question;
import com.korea.basic1.User.SiteUser;
import com.korea.basic1.User.UserRepository;
import com.korea.basic1.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    @PostMapping("/create/{nickname}")
    public String create(@PathVariable("nickname") String nickname) throws UnsupportedEncodingException {
        String encodedNickname = URLEncoder.encode(nickname, "UTF-8");
        SiteUser siteUser = this.userService.getUserByNickname(nickname);
        ChatRoom chatRoom = this.chatRoomService.saveDefaultChatRoomNum(siteUser);
        Integer id = chatRoom.getId();
        return "redirect:/chatbotList/" + encodedNickname + "/" + id;
    }

    @GetMapping("/modify/{nickname}/{id}")
    public String chatRoomModify(ChatRoomForm chatRoomForm, @PathVariable("nickname") String nickname, @PathVariable("id") Integer id) {
        ChatRoom chatRoom = this.chatRoomService.getChatRoom(id);
        chatRoomForm.setRoomName(chatRoom.getRoomName());
        return "chatbot/chatbot_form";
    }

    @PostMapping("/modify/{nickname}/{id}")
    public String chatRoomModify(@Valid ChatRoomForm chatRoomForm, BindingResult bindingResult, @PathVariable("nickname") String nickname,
                               @PathVariable("id") Integer id, Principal principal) throws UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            return "chatbot/chatbot_form";
        }
        String encodedNickname = URLEncoder.encode(nickname, "UTF-8");
        ChatRoom chatRoom = this.chatRoomService.getChatRoom(id);
        this.chatRoomService.modify(chatRoom, chatRoomForm.getRoomName());
        return "redirect:/chatbotList/" + encodedNickname + "/" + id;
    }

    @GetMapping("/delete/{nickname}/{id}")
    public String chatRoomDelete(@PathVariable("nickname") String nickname, @PathVariable("id") Integer id) throws UnsupportedEncodingException {
        ChatRoom chatRoom = this.chatRoomService.getChatRoom(id);
        String encodedNickname = URLEncoder.encode(nickname, "UTF-8");
        this.chatRoomService.delete(chatRoom);
        return "redirect:/chatbot/" + encodedNickname;
    }
}
