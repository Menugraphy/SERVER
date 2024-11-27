package com.menugraphy.server.domain.menu.service;

import com.menugraphy.server.domain.menu.model.dto.ImageRequest;
import com.menugraphy.server.domain.menu.model.dto.ImageResponse;
import com.menugraphy.server.global.external.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    //    private final MenuRepository menuRepository;
    private final StorageService storageService;

    @Transactional
    public ImageResponse uploadImage(
            ImageRequest imageRequest
    ) {
        String fileUrl = storageService.uploadFile(imageRequest.image());

        return ImageResponse.of(fileUrl);
    }
}
