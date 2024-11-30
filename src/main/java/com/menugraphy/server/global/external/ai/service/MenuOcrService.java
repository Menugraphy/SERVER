package com.menugraphy.server.global.external.ai.service;

import com.menugraphy.server.global.external.ai.dto.MenuResultResponse;
import com.menugraphy.server.global.external.ai.dto.MenuUploadResponse;
import com.menugraphy.server.global.external.ai.feign.MenuResultFeignClient;
import com.menugraphy.server.global.external.ai.feign.MenuUploadFeignClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuOcrService {

    private final MenuUploadFeignClient menuUploadFeignClient;
    private final MenuResultFeignClient menuResultFeignClient;

    public String fetchResultPath(final String key, final String extension) {
        final MenuUploadResponse response = uploadMenuImage(key, extension);
        return response.resultPath();
    }

    private MenuUploadResponse uploadMenuImage(final String key, final String extension) {
        return menuUploadFeignClient.postMenuBoardImage(key, extension);
    }

    public List<MenuResultResponse> fetchMenuResult(final String imageKey) {
        return menuResultFeignClient.getMenuBoardResult(imageKey);
    }
}
