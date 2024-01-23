package com.korea.basic1.User;

import com.korea.basic1.chatbotRoom.ChatRoom;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userid;

    @Column(unique = true)
    private String nickname;

    private String password;

    private String email;

    private String role;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<ChatRoom> chatRoom;
}