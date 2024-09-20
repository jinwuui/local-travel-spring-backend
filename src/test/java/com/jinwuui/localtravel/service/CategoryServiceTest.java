package com.jinwuui.localtravel.service;

import com.jinwuui.localtravel.domain.Category;
import com.jinwuui.localtravel.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void clean() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리 생성 또는 찾기 - 생성인 경우")
    void findOrCreateCategoryWhenNewCategory() {
        // given
        String name = "카테고리1";

        // when
        Category category = categoryService.findOrCreate(name);

        // then
        assertEquals(1L, categoryRepository.count());
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("카테고리 생성 또는 찾기 - 찾기인 경우")
    void findOrCreateCategoryWhenExistingCategory() {
        // given
        Category category = Category.builder()
                .name("카테고리1")
                .build();
        categoryRepository.save(category);

        // when
        Category findCategory = categoryService.findOrCreate(category.getName());

        // then
        assertEquals(1L, categoryRepository.count());
        assertEquals(category.getId(), findCategory.getId());
        assertEquals(category.getName(), findCategory.getName());
    }

    @Test
    @DisplayName("카테고리 생성")
    void createCategory() {
        // given
        String name = "카테고리1";

        // when
        Category category = categoryService.create(name);

        // then
        assertEquals(1L, categoryRepository.count());
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("카테고리 생성 실패 - 이름이 null")
    void createCategoryFailNameIsNull() {
        // given
        String name = null;

        // expected
        assertThrows(DataIntegrityViolationException.class,
                () -> categoryService.create(name));
    }

    @Test
    @DisplayName("카테고리 생성 실패 - 이름이 unique하지 않을때")
    void createCategoryFailNameNotUnique() {
        // given
        Category category1 = Category.builder()
                .name("카테고리1")
                .build();
        categoryRepository.save(category1);

        // expected
        assertThrows(DataIntegrityViolationException.class,
                () -> categoryService.create(category1.getName()));
    }
}