package com.korea.basic1.Answer;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Page<Answer> findAll(Pageable pageable);
}
