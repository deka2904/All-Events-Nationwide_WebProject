package com.korea.basic1.Category;

import com.korea.basic1.DataNotFoundException;
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

}
