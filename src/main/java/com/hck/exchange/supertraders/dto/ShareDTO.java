package com.hck.exchange.supertraders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by @hck
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareDTO {
    private String symbol;
    private BigDecimal rate;
    private Boolean isRegistered;
}
