package com.menugraphy.server.domain.menu.repository;

import com.menugraphy.server.domain.menu.model.entity.MenuBoard;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuBoardRepository extends JpaRepository<MenuBoard, Long> {

    boolean existsByImage(String image);

    Optional<MenuBoard> findByImage(String image);

    default MenuBoard findByImageByIdOrThrow(String image) {
        return findByImage(image)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_MENUBOARD_ERROR));
    }
}
