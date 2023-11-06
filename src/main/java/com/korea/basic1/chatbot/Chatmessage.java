package com.korea.basic1.chatbot;

import com.korea.basic1.chatbotRoom.ChatRoom;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Chatmessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String query;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String answer;

    @ManyToOne
    private ChatRoom chatRoom;

    private LocalDateTime createDate;
}
