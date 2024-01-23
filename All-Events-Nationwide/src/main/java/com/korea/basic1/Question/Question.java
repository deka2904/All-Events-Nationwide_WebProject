package com.korea.basic1.Question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.korea.basic1.Answer.Answer;
import com.korea.basic1.Category.Category;
import com.korea.basic1.User.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(200) NOT NULL")
    private String subject;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String content;

    @Column(columnDefinition = "VARCHAR(200) NOT NULL")
    private String postcode;

    @Column(columnDefinition = "VARCHAR(200) NOT NULL")
    private String roadAddress;

    @Column(columnDefinition = "VARCHAR(200) NOT NULL")
    private String jibunAddress;

    @Column(columnDefinition = "VARCHAR(200) NOT NULL")
    private String detailAddress;

    @Column(columnDefinition = "VARCHAR(200) NOT NULL")
    private String extraAddress;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private Category category;

    private LocalDateTime modifyDate;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

    private String filepath;

    private String filename;
}