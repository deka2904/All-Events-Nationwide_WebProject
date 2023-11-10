package com.korea.basic1.chatbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korea.basic1.User.SiteUser;
import com.korea.basic1.User.UserRepository;
import com.korea.basic1.User.UserService;
import com.korea.basic1.chatbotRoom.ChatRoom;
import com.korea.basic1.chatbotRoom.ChatRoomService;
import groovy.lang.GString;
import groovy.lang.Interceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatmessageController {
    private final ChatmessageService chatmessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    @GetMapping("/chatbot/{nickname}")
    public String main(Model model, @PathVariable("nickname") String nickname) {
        SiteUser siteUser = this.userService.getUserByNickname(nickname);
        if (siteUser != null) {
            List<ChatRoom> chatRoomList = siteUser.getChatRoom();
            model.addAttribute("chatRoomList", chatRoomList);
        } else {
            model.addAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
        }
        return "chatbot/chatbot_main";
    }

    @GetMapping("/chatbotList/{nickname}/{id}")
    public String main(Model model, @PathVariable("nickname") String nickname, @PathVariable("id") Integer id, ChatRoom chatRoom) {
        SiteUser siteUser = this.userService.getUserByNickname(nickname);

        if (siteUser != null) {
            List<ChatRoom> chatRoomList = siteUser.getChatRoom();
            model.addAttribute("chatRoomList", chatRoomList);
        } else {
            model.addAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
        }

        List<Chatmessage> chatmessageList = this.chatmessageService.getListByRoomId(chatRoom);
        ChatRoom name = this.chatRoomService.getChatRoom(id);
        model.addAttribute("chatmessageList", chatmessageList);
        model.addAttribute("id", id);
        model.addAttribute("name", name.getRoomName());
        return "chatbot/chatbot_list";
    }

    @PostMapping("/chatbotList/{nickname}/{id}")
    public String sendDataToServer(String query, @PathVariable("nickname") String nickname, @PathVariable("id") Integer id) throws UnsupportedEncodingException  {
        String encodedNickname = URLEncoder.encode(nickname, "UTF-8");
        try {
            String data = String.format("{\"query\":\"%s\"}", HtmlUtils.htmlEscape(query));
            RestTemplate restTemplate = new RestTemplate();
            String serverAUrl = "http://127.0.0.1:5000/query/TEST";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> chatRequest = new HttpEntity<>(data, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(serverAUrl, chatRequest, String.class);
            System.out.println(response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
                    String AnswerData = (String) responseMap.get("Answer");
                    String QueryData = (String) responseMap.get("Query");

                    this.chatmessageService.create(QueryData, AnswerData, id);

                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // 다른 상태 코드에 대한 처리
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 처리
        }
        return "redirect:/chatbotList/" + encodedNickname + "/" + id;
    }
}
