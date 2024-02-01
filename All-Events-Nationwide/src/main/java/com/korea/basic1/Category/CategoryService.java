package com.korea.basic1.Category;

import com.korea.basic1.DataNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public Category getCategory(Integer id) {
        Optional<Category> category = this.categoryRepository.findById(id);

        if (category.isPresent()) {
            return category.get();
        } else {
            throw new DataNotFoundException("category not found");
        }
    }
    @PostConstruct
    public void init() {
        saveDefaultCategory();
    }


    public void saveDefaultCategory() {
        if (categoryRepository.findByCategory("자유게시판") == null) {
            Category category = new Category();
            category.setCategory("자유게시판");
            categoryRepository.save(category);
        }

        if (categoryRepository.findByCategory("행사게시판") == null) {
            Category category = new Category();
            category.setCategory("행사게시판");
            categoryRepository.save(category);
        }

        if (categoryRepository.findByCategory("문의사항") == null) {
            Category category = new Category();
            category.setCategory("문의사항");
            categoryRepository.save(category);
        }

        if (categoryRepository.findByCategory("Q&A") == null) {
            Category category = new Category();
            category.setCategory("Q&A");
            categoryRepository.save(category);
        }
    }
}
