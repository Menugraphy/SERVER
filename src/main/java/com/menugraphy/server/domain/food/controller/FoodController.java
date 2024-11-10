package com.menugraphy.server.domain.food.controller;

import com.menugraphy.server.domain.food.model.dto.CategoryListResponse;
import com.menugraphy.server.domain.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/food")
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/types")
    public ResponseEntity<CategoryListResponse> getFoodTypes() {
        return ResponseEntity.ok(foodService.fetchCategoryTypes());
    }
}
