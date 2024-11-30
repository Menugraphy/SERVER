package com.menugraphy.server.global.external.ai.feign;

import com.menugraphy.server.global.config.FeignConfig;
import com.menugraphy.server.global.external.ai.dto.MenuUploadResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "menu-upload", url = "${feign.client.url.menu}", configuration = FeignConfig.class)
public interface MenuUploadFeignClient {

    @PostMapping("/menu")
    MenuUploadResponse postMenuBoardImage(
            @RequestParam("key") final String key,
            @RequestParam("extension") final String extension
    );
}
