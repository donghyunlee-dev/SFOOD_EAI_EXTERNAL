package com.sfood.eai.external.scheduler.job;

import com.sfood.eai.external.scheduler.GodoMallOrderScheduler;
import com.sfood.eai.external.scheduler.ScheduleProperties;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class GodoMallJob extends QuartzJobBean {

    @Autowired
    private GodoMallOrderScheduler godoMallOrderScheduler;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        ScheduleProperties properties = ScheduleProperties.createFrom(context);

        switch (properties.getCollectType()) {
            case "S", "S1" -> godoMallOrderScheduler.collectOrders(properties);
            case "C", "C1" -> godoMallOrderScheduler.collectClaims(properties);
            case "S2" -> godoMallOrderScheduler.collectOrdersByOrderNo(properties);
            case null, default -> log.error("GodoMall - Unsupported collect type: {}", properties.getCollectType());
        }
    }
}
