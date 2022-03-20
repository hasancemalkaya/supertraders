package com.hck.exchange.supertraders.job;

import com.hck.exchange.supertraders.entity.ShareUpdate;
import com.hck.exchange.supertraders.service.TradeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

/**
 * Created by @hck
 */

@Log4j2
public class ShareRateUpdateJob extends ThreadPoolTaskExecutor {

    @Autowired
    private transient TradeService tradeService;

    public ShareRateUpdateJob(int corePoolsSize) {
        log.info("ShareRateUpdateJob initializing...");
        setCorePoolSize(corePoolsSize);
        setThreadNamePrefix("ShareRateUpdateJob-exec-");
        initialize();
    }

    public void update() {
        List<ShareUpdate> list = tradeService.getAllShareUpdates();

        list.forEach(shareUpdate ->
                execute(() -> setNewShareRate(shareUpdate))
        );
    }

    public void setNewShareRate(ShareUpdate shareUpdate) {
        tradeService.setNewShareRate(shareUpdate);
    }
}
