package com.menugraphy.server.domain.food.repository;

import com.menugraphy.server.domain.food.model.entity.Food;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {

    boolean existsByName(String menuName);

    Optional<Food> findFoodByName(String menuName);

    default Food findFoodByNameOrThrow(String menuName) {
        return findFoodByName(menuName)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FOOD_NAME_ERROR));
    }

    Optional<Food> findFoodById(Long menuId);

    default Food findFoodByIdOrThrow(Long menuId) {
        return findFoodById(menuId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FOOD_ID_ERROR));
    }
}
