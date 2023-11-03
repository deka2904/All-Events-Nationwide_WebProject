package com.korea.basic1.Question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

    Page<Question> findAll(Pageable pageable);
    Page<Question> findByCategoryId(int id, Pageable pageable);

    @Query("select distinct q from Question q where q.subject like %:kw%")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
