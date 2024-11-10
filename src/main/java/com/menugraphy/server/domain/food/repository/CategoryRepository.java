package com.menugraphy.server.domain.food.repository;

import com.menugraphy.server.domain.food.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
