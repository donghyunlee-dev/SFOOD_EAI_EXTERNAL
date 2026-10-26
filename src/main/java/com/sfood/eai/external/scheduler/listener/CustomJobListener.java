package com.sfood.eai.external.scheduler.listener;


import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;

@Slf4j
public class CustomJobListener implements JobListener {

    @Override
    public String getName() {
        return "BatchJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        log.info("jobToBeExecuted job key : " + jobKey.toString());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        log.info("jobExecutionVetoed job key : " + jobKey.toString());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        log.info("jobWasExecuted job key : " + jobKey.toString());
    }
}
