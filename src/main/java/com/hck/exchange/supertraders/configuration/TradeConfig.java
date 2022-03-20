package com.hck.exchange.supertraders.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by @hck
 */

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "trade.config")
public class TradeConfig {
    private int shareRateUpdateJobCorePoolSize;
}
