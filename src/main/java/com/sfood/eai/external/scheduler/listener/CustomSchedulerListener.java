package com.sfood.eai.external.scheduler.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

@Slf4j
public class CustomSchedulerListener implements SchedulerListener {

    @Override
    public void jobScheduled(Trigger trigger) {
       log.info("Job scheduled : {}, cron : {}", trigger.getKey().getName(), ((CronTrigger) trigger).getCronExpression());
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        log.info("Job unscheduled : {}", triggerKey.getName());
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        log.info("Trigger finalized : {}", trigger.getKey().getName());
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        log.info("Trigger paused : {}", triggerKey.getName());
    }

    @Override
    public void triggersPaused(String triggerGroup) {
        log.info("Triggers paused for group : {}", triggerGroup);
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        log.info("Trigger resumed : {}", triggerKey.getName());
    }

    @Override
    public void triggersResumed(String triggerGroup) {
        log.info("Triggers resumed for group : {}", triggerGroup);
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
        log.info("Job added : {}", jobDetail.getKey().toString());
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        log.info("Job deleted : {}", jobKey.toString());
    }

    @Override
    public void jobPaused(JobKey jobKey) {
        log.info("Job paused: {}", jobKey.toString());
    }

    @Override
    public void jobsPaused(String jobGroup) {
        log.info("Jobs paused for group : {}", jobGroup);
    }

    @Override
    public void jobResumed(JobKey jobKey) {
        log.info("Job resumed : {}", jobKey.toString());
    }

    @Override
    public void jobsResumed(String jobGroup) {
        log.info("Jobs resumed for group : {}", jobGroup);
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
        log.info("Scheduler error : " + msg);
        cause.printStackTrace();
    }

    @Override
    public void schedulerInStandbyMode() {
        log.info("Scheduler in standby mode.");
    }

    @Override
    public void schedulerStarted() {
        log.info("Scheduler started.");
    }

    @Override
    public void schedulerStarting() {
        System.out.println("Scheduler starting.");
    }

    @Override
    public void schedulerShutdown() {
        System.out.println("Scheduler shutdown.");
    }

    @Override
    public void schedulerShuttingdown() {
        System.out.println("Scheduler shutting down.");
    }

    @Override
    public void schedulingDataCleared() {
        System.out.println("Scheduling data cleared.");
    }
}