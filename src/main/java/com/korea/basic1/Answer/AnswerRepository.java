package com.korea.basic1.Answer;

import com.korea.basic1.Question.Question;
import com.korea.basic1.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Page<Answer> findAll(Pageable pageable);

    @RequiredArgsConstructor
    @Service
    class AnswerService {

        private final AnswerRepository answerRepository;

        public Page<Answer> getList(int page) {
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("createDate"));
            Pageable pageable = PageRequest.of(page, 2, Sort.by(sorts));
            return this.answerRepository.findAll(pageable);
        }
        public void create(Question question, String content, SiteUser author) {
            Answer answer = new Answer();
            answer.setContent(content);
            answer.setCreateDate(LocalDateTime.now());
            answer.setQuestion(question);
            answer.setAuthor(author);
            this.answerRepository.save(answer);
        }
    }
}
