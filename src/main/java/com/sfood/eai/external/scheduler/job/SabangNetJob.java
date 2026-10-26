package com.sfood.eai.external.scheduler.job;

import com.sfood.eai.external.scheduler.SabangNetOrderScheduler;
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
public class SabangNetJob extends QuartzJobBean {

    @Autowired
    private SabangNetOrderScheduler sabangNetOrderScheduler;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ScheduleProperties properties = ScheduleProperties.createFrom(context);
        sabangNetOrderScheduler.collectOrders(properties);
    }
}
