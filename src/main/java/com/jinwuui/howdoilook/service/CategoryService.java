package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.Category;
import com.jinwuui.howdoilook.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category create(String name) {
        Category category = Category.builder()
                .name(name)
                .build();

        return categoryRepository.save(category);
    }
}
