package com.menugraphy.server.domain.food.repository;

import com.menugraphy.server.domain.food.model.entity.Type;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Long> {

    List<Type> findAllByCategoryId(Long categoryId);
}
