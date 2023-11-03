package com.korea.basic1.chatbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ServerController {
    private List<String> answerList = new ArrayList<>();
    private List<String> queryList = new ArrayList<>();

    @GetMapping("/chatbot")
    public String main(Model model){
        model.addAttribute("answerList", answerList);
        model.addAttribute("queryList", queryList);
        return "chat";
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

                answerList.add(AnswerData);
                queryList.add(QueryData);
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
