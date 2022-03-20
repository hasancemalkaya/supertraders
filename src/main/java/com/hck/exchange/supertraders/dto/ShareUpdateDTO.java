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
public class ShareUpdateDTO {
    private String symbol;
    private BigDecimal newRate;
}
