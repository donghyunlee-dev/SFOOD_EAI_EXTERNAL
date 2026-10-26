package com.sfood.eai.external.entity.subscription;

import com.sfood.eai.external.entity.id.subscription.SubscriptionDetailTurnId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@IdClass(SubscriptionDetailTurnId.class)
@Entity
@Table(name="TBL_SUBSCRIPTION_DETAIL_TURN")
public class SubscriptionDetailTurn extends BaseEntity {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Id
    @Column(name = "SCHEDULE_CODE")
    private String scheduleCode;

    @Id
    @Column(name = "TURN_NO")
    private String turnNo;

    @Id
    @Column(name = "GOODS_CODE")
    private String goodsCode;

    @Column(name = "GOODS_CNT")
    private Integer goodsCnt;

    @Column(name = "EXT1")
    private String ext1;

    @Column(name = "EXT2")
    private String ext2;
}
