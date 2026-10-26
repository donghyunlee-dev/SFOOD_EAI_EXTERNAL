package com.sfood.eai.external.scheduler;

import com.sfood.eai.external.util.CustomDateUtils;
import lombok.Getter;
import lombok.ToString;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

@ToString
@Getter
public class ScheduleProperties {
    /**
     * 회사 코드
     */
    private String companyCode;
    /**
     * 연동 채널 코드
     */
    private String channelCode;
    /**
     * 연동몰 코드
     */
    private String mallCode;
    /**
     * 연동몰 사용자 코드
     */
    private String inMallCode;
    /**
     * 수집 구분
     */
    private String collectType;
    /**
     * 수집 일자
     */
    private String collectDate;
    /**
     * API 호출 사용자 ID
     * batch 고정 값
     */
    private final String userId = "scheduler";

    private ScheduleProperties() {}

    public static ScheduleProperties createFrom(JobExecutionContext context) {

        ScheduleProperties newInstance = new ScheduleProperties();

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        newInstance.companyCode = dataMap.getString("companyCode");
        newInstance.channelCode = context.getJobDetail().getKey().getGroup().split("\\.")[1];
        newInstance.mallCode = dataMap.getString("mallCode");
        newInstance.inMallCode = dataMap.getString("inMallCode");
        newInstance.collectType = dataMap.getString("collectType");

        if (newInstance.collectType.contains("1")) {
            newInstance.collectDate = CustomDateUtils.addDays(-1);
        } else {
            newInstance.collectDate = CustomDateUtils.now();
        }

        return newInstance;
    }
}
