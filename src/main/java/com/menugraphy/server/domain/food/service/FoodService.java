package com.menugraphy.server.domain.food.service;

import com.menugraphy.server.domain.food.model.dto.CategoryListResponse;
import com.menugraphy.server.domain.food.model.dto.CategoryResponse;
import com.menugraphy.server.domain.food.model.dto.TypeResponse;
import com.menugraphy.server.domain.food.model.entity.Category;
import com.menugraphy.server.domain.food.model.entity.Type;
import com.menugraphy.server.domain.food.repository.CategoryRepository;
import com.menugraphy.server.domain.food.repository.TypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final CategoryRepository categoryRepository;
    private final TypeRepository typeRepository;

    @Transactional(readOnly = true)
    public CategoryListResponse fetchCategoryTypes() {
        // 모든 카테고리와 타입 가져오기
        List<Category> categories = categoryRepository.findAll();
        List<Type> types = typeRepository.findAll();

        // Category와 관련된 Type을 매칭
        List<CategoryResponse> categoryList = categories.stream()
                .map(category -> {
                    List<TypeResponse> typeList = types.stream()
                            .filter(type -> type.getCategoryId() == category.getId()) // categoryId로 매칭
                            .map(type -> TypeResponse.of(type.getId(), type.getName()))
                            .toList();

                    return CategoryResponse.of(category.getId(), category.getName(), typeList);
                })
                .filter(categoryResponse -> !categoryResponse.typeList().isEmpty()) // 비어 있지 않은 카테고리만 추가
                .toList();

        return CategoryListResponse.of(categoryList);
    }
}
