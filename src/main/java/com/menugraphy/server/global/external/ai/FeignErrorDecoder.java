package com.menugraphy.server.global.external.ai;

import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 422) {
            return new CustomException(ErrorType.VALIDATION_ERROR);
        }
        return new CustomException(ErrorType.INTERNAL_FEIGN_ERROR);
    }
}
