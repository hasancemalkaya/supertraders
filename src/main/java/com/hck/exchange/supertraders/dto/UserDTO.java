package com.hck.exchange.supertraders.dto;

import com.hck.exchange.supertraders.entity.Portfolio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by @hck
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userId;
    private String name;
    private PortfolioDTO portfolio;
}
