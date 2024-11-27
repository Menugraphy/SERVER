package com.menugraphy.server.domain.menu.model.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ImageRequest(
        @NotNull(message = "이미지는 필수입니다.")
        MultipartFile image
) {

}
