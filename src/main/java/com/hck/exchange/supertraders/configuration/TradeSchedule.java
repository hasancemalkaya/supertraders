package com.hck.exchange.supertraders.configuration;

import com.hck.exchange.supertraders.job.ShareRateUpdateJob;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by @hck
 */

@Log4j2
@Configuration
@EnableScheduling
@AllArgsConstructor
public class TradeSchedule implements SchedulingConfigurer {

    private TradeConfig tradeConfig;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(executor());
    }

    @Bean
    public Executor executor() {
        return Executors.newScheduledThreadPool(5);
    }

    @Bean
    public ShareRateUpdateJob shareRateUpdateJob() {
        return  new ShareRateUpdateJob(tradeConfig.getShareRateUpdateJobCorePoolSize());
    }

    @Async
    @Scheduled(cron = "${trade.config.shareRateUpdateJobCron}", zone = "Europe/Istanbul")
    public void scheduledDecodeTransparentData() {
        shareRateUpdateJob().update();
    }

}
