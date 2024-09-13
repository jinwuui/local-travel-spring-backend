package com.jinwuui.howdoilook.repository;

import com.jinwuui.howdoilook.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
