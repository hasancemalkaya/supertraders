package com.hck.exchange.supertraders.dto;

import com.hck.exchange.supertraders.type.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by @hck
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeDTO {

    private TradeType type;

    private String userId;
    private String share;
    private Integer amount;
    private Date date;
}
