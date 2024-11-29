package com.menugraphy.server.domain.food.repository;

import com.menugraphy.server.domain.food.model.entity.Type;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Long> {

    List<Type> findAllByCategoryId(Long categoryId);

    Optional<Type> findTypeById(Long typeId);

    default Type findTypeByIdOrThrow(Long typeId) {
        return findTypeById(typeId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_MEMBER_ERROR));
    }
}
