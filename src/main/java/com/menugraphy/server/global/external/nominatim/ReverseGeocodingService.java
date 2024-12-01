package com.menugraphy.server.global.external.nominatim;

import com.menugraphy.server.global.external.nominatim.dto.NominatimResponse;
import com.menugraphy.server.global.external.nominatim.feign.NominatimFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReverseGeocodingService {

    private final NominatimFeignClient nominatimFeignClient;

    public String fetchDisplayName(final double latitude, final double longitude) {
        NominatimResponse response = nominatimFeignClient.reverseGeocode(latitude, longitude, "json");
        return response.displayName();
    }
}
