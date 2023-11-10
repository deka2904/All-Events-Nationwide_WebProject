package com.korea.basic1.chatbotRoom;

import com.korea.basic1.User.SiteUser;
import com.korea.basic1.chatbot.Chatmessage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String RoomName;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<Chatmessage> chatmessageList;

    @ManyToOne
    private SiteUser siteUser;
}
