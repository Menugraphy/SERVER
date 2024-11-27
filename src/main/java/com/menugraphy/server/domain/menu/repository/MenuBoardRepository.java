package com.menugraphy.server.domain.menu.repository;

import com.menugraphy.server.domain.menu.model.entity.MenuBoard;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuBoardRepository extends JpaRepository<MenuBoard, Long> {

    boolean existsByAfterImage(String image);

    Optional<MenuBoard> findByAfterImage(String image);

    default MenuBoard findByAfterImageByIdOrThrow(String image) {
        return findByAfterImage(image)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_MENUBOARD_ERROR));
    }

    Optional<MenuBoard> findMenuBoardById(Long imageId);

    default MenuBoard findMenuBoardByIdOrThrow(Long imageId) {
        return findMenuBoardById(imageId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_MENUBOARD_ERROR));
    }
}
