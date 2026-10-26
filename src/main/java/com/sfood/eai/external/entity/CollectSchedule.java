package com.sfood.eai.external.entity;

import com.sfood.eai.external.entity.id.CollectScheduleId;
import com.sfood.eai.external.util.CustomTimeUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_COLLECT_SCHEDULE_MST")
@Entity
public class CollectSchedule {

    @EmbeddedId
    private CollectScheduleId id;

    @Column(name = "MALL_CODE")
    private String mallCode;

    @Column(name = "IN_MALL_CODE")
    private String inMallCode;

    @Column(name = "COLLECT_TYPE")
    private String collectType;

    @Column(name = "SCHEDULE_TYPE")
    private String scheduleType;

    @Column(name = "COLLECT_START_DATETIME")
    private LocalDateTime collectStartDateTime;

    @Column(name = "SCHEDULE_TERM_MIN")
    private Integer scheduleTermMin;

    @Column(name = "SCHEDULE_TIME")
    private LocalTime scheduleTime;

    @Column(name = "COLLECT_YN")
    private String collectYn;

    @Transient
    private String cronExpression;

    public String getCronExpression() {

        LocalTime scheduleTime;

        if ("C".equals(this.scheduleType)) {
            scheduleTime = this.collectStartDateTime.toLocalTime();
        } else {
            scheduleTime = this.scheduleTime;
        }

        return CustomTimeUtils.convertToCron(this.scheduleType, scheduleTime, this.scheduleTermMin == null ? 0 : this.scheduleTermMin);
    }
}
