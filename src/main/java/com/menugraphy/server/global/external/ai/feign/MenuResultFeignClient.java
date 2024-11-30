package com.menugraphy.server.global.external.ai.feign;

import com.menugraphy.server.global.config.FeignConfig;
import com.menugraphy.server.global.external.ai.dto.MenuResultResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "menu-result", url = "${feign.client.url.menu}", configuration = FeignConfig.class)
public interface MenuResultFeignClient {

    @GetMapping("/menu/{image_key}")
    List<MenuResultResponse> getMenuBoardResult(
            @PathVariable("image_key") final String imageKey
    );
}
