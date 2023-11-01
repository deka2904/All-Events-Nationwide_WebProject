package com.korea.basic1;

import com.korea.basic1.Category.Category;
import com.korea.basic1.Category.CategoryRepository;
import com.korea.basic1.Question.Question;
import com.korea.basic1.Question.QuestionRepository;
import com.korea.basic1.Question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class Basic1ApplicationTests {
	@Autowired
	private CategoryRepository categoryRepository;
	private QuestionRepository questionRepository;
	@Test
	void contextLoads() {
//		Question q = new Question();
//		q.setSubject("new_subject");
//		q.setContent("new_content");
//		q.setCreateDate(LocalDateTime.now());
//		q.setCategory();
//		this.questionRepository.save(q);

	}
}
