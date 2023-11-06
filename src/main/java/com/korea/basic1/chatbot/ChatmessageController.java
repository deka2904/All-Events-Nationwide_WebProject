package com.korea.basic1.chatbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korea.basic1.chatbotRoom.ChatRoom;
import com.korea.basic1.chatbotRoom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatmessageController {
    private final ChatmessageService chatmessageService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/chatbot")
    public String main(Model model){
        List<Chatmessage> chatmessageList = this.chatmessageService.getList();
        List<ChatRoom> chatRoomList = this.chatRoomService.getList();
        model.addAttribute("chatmessageList", chatmessageList);
        model.addAttribute("chatRoomList", chatRoomList);
        return "chatbot";
    }

    @GetMapping("/chatbot/{id}")
    public String main(Model model, ChatRoom chatRoom){
        List<Chatmessage> chatmessageList = this.chatmessageService.getListByRoomId(chatRoom);
        List<ChatRoom> chatRoomList = this.chatRoomService.getList();
        model.addAttribute("chatmessageList", chatmessageList);
        model.addAttribute("chatRoomList", chatRoomList);
        return "chatbot";
    }

    @PostMapping("/chatbot")
    public String sendDataToServer(Model model, String query) {
        String data = String.format("{\"query\":\"%s\"}", query);
        RestTemplate restTemplate = new RestTemplate();
        String serverAUrl = "http://127.0.0.1:5000/query/TEST";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> chatRequest = new HttpEntity<>(data, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(serverAUrl, chatRequest, String.class);
        System.out.println(response.getBody());
        if(response.getStatusCode()== HttpStatus.OK){
            String resopnseBody = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            try{
                Map<String, Object> responseMap = objectMapper.readValue(resopnseBody,  new TypeReference<Map<String, Object>>(){});
                String AnswerData = (String) responseMap.get("Answer");
                String QueryData = (String) responseMap.get("Query");

                this.chatmessageService.create(QueryData,AnswerData);

            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
        }
        return "redirect:/chatbot";
    }
}
