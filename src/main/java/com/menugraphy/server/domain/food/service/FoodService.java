package com.menugraphy.server.domain.food.service;

import com.menugraphy.server.domain.food.model.dto.CategoryListResponse;
import com.menugraphy.server.domain.food.model.dto.CategoryResponse;
import com.menugraphy.server.domain.food.model.dto.TypeResponse;
import com.menugraphy.server.domain.food.model.entity.Category;
import com.menugraphy.server.domain.food.model.entity.Type;
import com.menugraphy.server.domain.food.repository.CategoryRepository;
import com.menugraphy.server.domain.food.repository.TypeRepository;
import java.util.ArrayList;
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
        List<CategoryResponse> categoryList = new ArrayList<>();
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            List<TypeResponse> typeList = new ArrayList<>();
            List<Type> types = typeRepository.findAllByCategoryId(category.getId());

            for (Type type : types) {
                typeList.add(new TypeResponse(type.getId(), type.getName()));
            }

            if (!typeList.isEmpty()) {
                categoryList.add(new CategoryResponse(category.getId(), category.getName(), typeList));
            }
        }

        return new CategoryListResponse(categoryList);
    }
}
