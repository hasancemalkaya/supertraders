package com.hck.exchange.supertraders.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * Created by @hck
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse {
    private HttpStatus httpStatus;
    private Object payload;
    private String message;
}
