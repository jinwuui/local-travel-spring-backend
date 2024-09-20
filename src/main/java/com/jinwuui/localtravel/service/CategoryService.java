package com.jinwuui.localtravel.service;

import com.jinwuui.localtravel.domain.Category;
import com.jinwuui.localtravel.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findOrCreate(String name) {
        Optional<Category> existingCategory = categoryRepository.findByName(name);

        return existingCategory.orElseGet(() -> create(name));
    }

    public Category create(String name) {
        Category category = Category.builder()
                .name(name)
                .build();

        return categoryRepository.save(category);
    }
}
