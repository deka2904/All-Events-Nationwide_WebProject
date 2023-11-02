package com.korea.basic1.Question;

import java.time.LocalDateTime;
import java.util.List;

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

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private Category category;

    @ManyToOne
    private SiteUser author;
}