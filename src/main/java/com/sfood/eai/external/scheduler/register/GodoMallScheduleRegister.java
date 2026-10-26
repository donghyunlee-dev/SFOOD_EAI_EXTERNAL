package com.sfood.eai.external.scheduler.register;

import com.sfood.eai.external.entity.CollectSchedule;
import com.sfood.eai.external.scheduler.job.GodoMallJob;
import com.sfood.eai.external.service.MarketMallService;
import com.sfood.eai.external.util.QuartzUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.quartz.Trigger.TriggerState.NORMAL;
import static org.quartz.Trigger.TriggerState.PAUSED;

@Slf4j
@Component
public class GodoMallScheduleRegister {
    private static final String CHANNEL_CODE = "CN0002";
    private final MarketMallService mallService;
    private final Scheduler scheduler;

    public GodoMallScheduleRegister(MarketMallService mallService, @Qualifier("godoMallScheduler") Scheduler scheduler) {
        this.mallService = mallService;
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void initialize() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }

    private void refreshSchedules() throws SchedulerException {

        for (CollectSchedule schedule : mallService.findScheduleByChannel(CHANNEL_CODE)) {

            String groupId = schedule.getId().getCompanyCode() + "." + CHANNEL_CODE;
            JobKey jobkey = JobKey.jobKey(schedule.getMallCode() + "." + schedule.getCollectType(), groupId);

            if (scheduler.checkExists(jobkey)) {

                TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(schedule.getId().getCollectScheduleSeq()), groupId);

                if (scheduler.checkExists(triggerKey)) {

                    Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);

                    if ("Y".equalsIgnoreCase(schedule.getCollectYn())) {

                        if (triggerState == PAUSED) {
                            scheduler.resumeTrigger(triggerKey);
                        }

                        Trigger existTrigger = scheduler.getTrigger(triggerKey);

                        if (!((CronTrigger) existTrigger).getCronExpression().equals(schedule.getCronExpression())) {
                            CronTrigger modifiedTrigger = QuartzUtils.addTrigger(schedule, triggerKey, existTrigger.getJobKey());
                            scheduler.rescheduleJob(triggerKey, modifiedTrigger);
                        }
                    } else {
                        if (triggerState != PAUSED) {
                            scheduler.pauseTrigger(triggerKey);
                        }

                        if (triggerState != NORMAL && triggerState != PAUSED) {
                            log.warn("Unexpected trigger state: {}", triggerState);
                        }
                    }
                } else {
                    scheduler.scheduleJob(QuartzUtils.addTrigger(schedule, triggerKey, jobkey));
                }
            } else {
                // Job 신규 등록
                if ("Y".equalsIgnoreCase(schedule.getCollectYn())) {
                    JobDetail jobDetail = QuartzUtils.newJobDetail(schedule, groupId, GodoMallJob.class);
                    Trigger trigger = QuartzUtils.newTrigger(schedule, groupId);
                    scheduler.scheduleJob(jobDetail, trigger);
                }
            }
        }

        //QuartzUtils.printScheduleInfo(scheduler);
    }

    @Scheduled(cron = "0 */5 * * * *")
    private void cleanUp() throws SchedulerException {

        List<JobKey> jobKeys = new ArrayList<>();

        scheduler.getJobGroupNames().forEach(groupName -> {
            try {
                jobKeys.addAll(scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName)));
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });

        if (CollectionUtils.isEmpty(jobKeys)) {
            return;
        }

        List<Trigger> allTriggers = new ArrayList<>();

        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
            allTriggers.addAll(triggersOfJob);
        }

        if (CollectionUtils.isEmpty(allTriggers)) {
            return;
        }

        List<CollectSchedule> scheduleByChannel = mallService.findScheduleByChannel(CHANNEL_CODE);

        if (CollectionUtils.isEmpty(scheduleByChannel)) {
            return;
        }

        allTriggers.forEach(trigger -> {
            try {
                if (!scheduleByChannel.stream().anyMatch(sch -> String.valueOf(sch.getId().getCollectScheduleSeq()).equals(trigger.getKey().getName()))) {

                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

                    if (triggerState != PAUSED) {
                        scheduler.pauseTrigger(trigger.getKey());
                    }
                }
            } catch (SchedulerException e) {
                log.error(e.getMessage());
            }
        });
    }

    @Scheduled(fixedDelay = 60000)
    private void refreshScheduledBatchJobs() throws SchedulerException {
        refreshSchedules();
    }
}
