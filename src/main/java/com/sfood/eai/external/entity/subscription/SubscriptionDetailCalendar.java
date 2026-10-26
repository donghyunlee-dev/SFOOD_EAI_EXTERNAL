package com.sfood.eai.external.entity.subscription;


import com.sfood.eai.external.entity.id.subscription.SubscriptionDetailCalendarId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@IdClass(SubscriptionDetailCalendarId.class)
@Entity
@Table(name="TBL_SUBSCRIPTION_DETAIL_CALENDAR")
public class SubscriptionDetailCalendar extends BaseEntity {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode;
    @Id
    @Column(name = "SCHEDULE_CODE")
    private String scheduleCode;
    @Id
    @Column(name = "SCHEDULE_DATE")
    private String scheduleDate;
    @Id
    @Column(name = "GOODS_CODE")
    private String goodsCode;
    @Id
    @Column(name = "SORT_NO")
    private String sortNo;
    @Column(name = "EXT1")
    private String ext1;
    @Column(name = "EXT2")
    private String ext2;
}
