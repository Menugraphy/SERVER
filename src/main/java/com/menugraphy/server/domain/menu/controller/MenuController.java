package com.menugraphy.server.domain.menu.controller;

import com.menugraphy.server.domain.menu.model.dto.ImageRequest;
import com.menugraphy.server.domain.menu.model.dto.ImageResponse;
import com.menugraphy.server.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
}
