package com.hck.exchange.supertraders.util;

import com.hck.exchange.supertraders.dto.RestResponse;
import org.springframework.http.HttpStatus;

/**
 * Created by @hck
 */
public class RestResponseUtil {

    private RestResponseUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static RestResponse.RestResponseBuilder success() {
        return success(null, null);
    }

    public static RestResponse.RestResponseBuilder success(Object payload) {
        return success(payload, null);
    }

    public static RestResponse.RestResponseBuilder success(Object payload, String message) {
        return RestResponse.builder().httpStatus(HttpStatus.OK)
                .payload(payload)
                .message(message);
    }

    public static RestResponse.RestResponseBuilder error(String message) {
        return error(null, message);

    }

    public static RestResponse.RestResponseBuilder error(Object payload, String message) {
        return RestResponse.builder().httpStatus(HttpStatus.BAD_REQUEST)
                .payload(payload)
                .message(message);

    }


}
