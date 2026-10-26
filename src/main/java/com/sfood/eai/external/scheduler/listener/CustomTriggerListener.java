package com.sfood.eai.external.scheduler.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

@Slf4j
public class CustomTriggerListener implements TriggerListener {

    @Override
    public String getName() {
        return "BatchTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        JobKey jobKey = trigger.getJobKey();
        log.info("Trigger fired Job Key : {} cron : {}", jobKey.toString(), ((CronTrigger) trigger).getCronExpression());
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        JobKey jobKey = trigger.getJobKey();
        log.info("Trigger Misfired Job Key : {} cron : {}", jobKey.toString(), ((CronTrigger) trigger).getCronExpression());
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        JobKey jobKey = trigger.getJobKey();
        log.info("triggerComplete Job Key : {} cron : {}", jobKey.toString(), ((CronTrigger) trigger).getCronExpression());
    }
}
