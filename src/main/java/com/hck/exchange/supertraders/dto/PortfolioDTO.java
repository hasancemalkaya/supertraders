package com.hck.exchange.supertraders.dto;

import com.hck.exchange.supertraders.entity.StockPortfolio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by @hck
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDTO {

    @Builder.Default
    private Set<StockPortfolio> stockPortfolio = new HashSet<>();
    private Boolean isRegistered;

}
