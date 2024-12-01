package com.menugraphy.server.global.external.nominatim.feign;

import com.menugraphy.server.global.external.nominatim.dto.NominatimResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "nominatimClient", url = "https://nominatim.openstreetmap.org")
public interface NominatimFeignClient {

    @GetMapping(value = "/reverse", consumes = "application/json")
    NominatimResponse reverseGeocode(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude,
            @RequestParam("format") String format
    );
}
