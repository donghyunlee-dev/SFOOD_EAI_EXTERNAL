package com.sfood.eai.external.entity.subscription;

import com.sfood.eai.external.entity.id.subscription.SubscriptionScheduleMasterId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@IdClass(SubscriptionScheduleMasterId.class)
@Entity
@Table(name="TBL_SUBSCRIPTION_SCHEDULE_MST")
public class SubscriptionScheduleMaster extends BaseEntity {

    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Id
    @Column(name = "SCHEDULE_CODE")
    private String scheduleCode;

    @Column(name = "SCHEDULE_NAME")
    private String scheduleName;

    @Column(name = "GOODS_CODE")
    private String goodsCode;

    @Column(name = "SCHEDULE_TYPE")
    private String scheduleType;

    @Column(name = "DELIVERY_TERM")
    private String deliveryTerm;

    @Column(name = "USE_YN")
    private String useYn;

    @Column(name = "DELIVERY_PERIOD")
    private int deliveryPeriod;

    @Column(name = "EXT2")
    private String ext2;
}
