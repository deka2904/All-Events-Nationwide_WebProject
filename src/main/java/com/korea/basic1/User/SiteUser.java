package com.korea.basic1.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(unique = true)
    private String email;
}