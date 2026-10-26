package com.sfood.eai.external.util;

import com.sfood.eai.external.entity.CollectSchedule;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.util.StringUtils;

import java.util.*;

public final class QuartzUtils {

    private QuartzUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    @SuppressWarnings("unchecked")
    public static JobDetail newJobDetail(CollectSchedule schedule, String groupName, Class clazz) {
        String jobName;

        if (StringUtils.hasText(schedule.getInMallCode())) {
            jobName = schedule.getMallCode() + "." + schedule.getInMallCode() + "." + schedule.getCollectType() ;
        } else {
            jobName = schedule.getMallCode() + "." + schedule.getCollectType();
        }

        return JobBuilder.newJob(clazz)
                .withIdentity(jobName, groupName)
                .usingJobData("companyCode", schedule.getId().getCompanyCode())
                .usingJobData("mallCode", schedule.getMallCode())
                .usingJobData("inMallCode", schedule.getInMallCode())
                .usingJobData("collectType", schedule.getCollectType())
                .build();
    }

    public static CronTrigger newTrigger(CollectSchedule schedule, String groupName) {
        return TriggerBuilder.newTrigger()
                .withIdentity(String.valueOf(schedule.getId().getCollectScheduleSeq()),  groupName)
                .withSchedule(
                        CronScheduleBuilder
                                .cronSchedule(schedule.getCronExpression())
                                .inTimeZone(TimeZone.getTimeZone("Asia/Seoul"))
                                .withMisfireHandlingInstructionFireAndProceed())
                .build();
    }


    public static CronTrigger addTrigger(CollectSchedule schedule, TriggerKey triggerKey, JobKey job) {
        return TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .forJob(job)
                .withSchedule(
                        CronScheduleBuilder
                                .cronSchedule(schedule.getCronExpression())
                                .inTimeZone(TimeZone.getTimeZone("Asia/Seoul"))
                                .withMisfireHandlingInstructionFireAndProceed())
                .build();
    }

    @SuppressWarnings("unchecked")
    public static void printScheduleInfo(Scheduler scheduler) throws SchedulerException {
        List<String> logLines = new ArrayList<>();

        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                JobDetail jobDetail = scheduler.getJobDetail(jobKey);

                String jobInfo = String.format("▶ Job - name: %s, group: %s, JobClass: %s", jobKey.getName(), jobKey.getGroup(), jobDetail.getJobClass().getName());
                logLines.add(jobInfo);

                List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    TriggerKey triggerKey = trigger.getKey();
                    Trigger.TriggerState state = scheduler.getTriggerState(triggerKey);

                    String cronExpr = (trigger instanceof CronTrigger)
                            ? ((CronTrigger) trigger).getCronExpression()
                            : "N/A";

                    String triggerInfo = String.format("   └─ Trigger - name: %s, group: %s, state: %s, cron: %s",
                            triggerKey.getName(), triggerKey.getGroup(), state.name(), cronExpr);

                    logLines.add(triggerInfo);
                }
            }
        }

        // 로그 출력
        logLines.forEach(System.out::println);
        System.out.println("-------------------------------------------------------");
    }
    @SuppressWarnings("unchecked")
    public void printDetailLog(Scheduler scheduler, String targetGroup, boolean showSummary) throws SchedulerException {
        Map<Trigger.TriggerState, Integer> stateCounter = new EnumMap<>(Trigger.TriggerState.class);
        List<String> logLines = new ArrayList<>();

        for (String groupName : scheduler.getJobGroupNames()) {
            if (targetGroup != null && !groupName.equals(targetGroup)) continue;

            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                logLines.add(String.format("▶ Job - name: %s, group: %s", jobKey.getName(), jobKey.getGroup()));

                List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    TriggerKey triggerKey = trigger.getKey();
                    Trigger.TriggerState state = scheduler.getTriggerState(triggerKey);
                    stateCounter.merge(state, 1, Integer::sum);

                    String cronExpr = (trigger instanceof CronTrigger)
                            ? ((CronTrigger) trigger).getCronExpression()
                            : "N/A";

                    logLines.add(String.format("   └─ Trigger - name: %s, group: %s, state: %s, cron: %s",
                            triggerKey.getName(), triggerKey.getGroup(), state.name(), cronExpr));
                }
            }
        }

        // 출력
        logLines.forEach(System.out::println);

        // 통계 요약
        if (showSummary) {
            System.out.println("Trigger State Summary:");
            for (Trigger.TriggerState state : Trigger.TriggerState.values()) {
                int count = stateCounter.getOrDefault(state, 0);
                System.out.printf("   - %s: %d%n", state.name(), count);
            }
        }
    }
}
