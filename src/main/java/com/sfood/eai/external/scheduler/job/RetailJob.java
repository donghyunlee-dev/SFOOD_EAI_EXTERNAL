package com.sfood.eai.external.scheduler.job;

import com.sfood.eai.external.scheduler.RetailOrderScheduler;
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
public class RetailJob extends QuartzJobBean {

    @Autowired
    private RetailOrderScheduler retailOrderScheduler;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ScheduleProperties properties = ScheduleProperties.createFrom(context);
        retailOrderScheduler.collectOrders(properties);
    }
}
