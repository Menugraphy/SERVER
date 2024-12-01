package com.menugraphy.server.domain.menu.controller;

import com.menugraphy.server.domain.menu.model.dto.ImageRequest;
import com.menugraphy.server.domain.menu.model.dto.ImageResponse;
import com.menugraphy.server.domain.menu.model.dto.MenuDetailResponse;
import com.menugraphy.server.domain.menu.model.dto.MenuListResponse;
import com.menugraphy.server.domain.menu.model.dto.OrderHistoryListResponse;
import com.menugraphy.server.domain.menu.model.dto.OrderScriptListRequest;
import com.menugraphy.server.domain.menu.model.dto.OrderScriptResponse;
import com.menugraphy.server.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menu")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/image")
    public ResponseEntity<ImageResponse> uploadImage(
            @Valid @ModelAttribute ImageRequest imageRequest
    ) {
        return ResponseEntity.ok(menuService.uploadImage(imageRequest));
    }

    @GetMapping("/image/{imageId}")
    public ResponseEntity<MenuListResponse> getRestructureMenuBoard(
            @Positive(message = "이미지 Id는 양수여야 합니다.")
            @PathVariable final Long imageId
    ) {
        return ResponseEntity.ok(menuService.restructureMenuBoard(imageId));
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDetailResponse> getMenuDetail(
            @Positive(message = "메뉴 Id는 양수여야 합니다.")
            @PathVariable final Long menuId
    ) {
        return ResponseEntity.ok(menuService.fetchMenuDetail(menuId));
    }

    @PostMapping("/order")
    public ResponseEntity<OrderScriptResponse> getOrderScript(
            @Valid @RequestBody OrderScriptListRequest menuOrderList
    ) {
        return ResponseEntity.ok(menuService.fetchOrderScript(menuOrderList));
    }

    @GetMapping("/order-histories")
    public ResponseEntity<OrderHistoryListResponse> getOrderHistories() {
        return ResponseEntity.ok(menuService.fetchOrderHistories());
    }
}
