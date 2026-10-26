package com.sfood.eai.external.entity.order;

import com.sfood.eai.external.entity.id.order.OrderClaimId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper=false)
@Data
@Entity
@Table(name = "TBL_ORDER_CLAIM_INFO")
public class OrderClaim extends BaseEntity {

    @EmbeddedId
    private OrderClaimId id;

    @Column(name = "CLAIM_DATE")
    private LocalDateTime claimDate;

    @Column(name = "CLAIM_REASON_MST_CODE")
    private String claimReasonMstCode;

    @Column(name = "CLAIM_REASON_DETAIL_CODE")
    private String claimReasonDetailCode;

    @Column(name = "CLAIM_REASON_CONTENT")
    private String claimReasonContent;

    @Column(name = "RETURN_REAL_YN")
    private String returnRealYn;

    @Column(name = "MARKET_CLAIM_ID")
    private String marketClaimId;

    @Column(name = "ORDER_CLAIM_RECEIVE_DATE")
    private LocalDateTime orderClaimReceiveDate;

    @Column(name = "ORDER_CLAIM_ACCEPT_DATE")
    private LocalDateTime orderClaimAcceptDate;

    @Column(name = "MARKET_STOP_YN")
    private String marketStopYn;

    public void changeId(OrderClaimId id) {
        this.id = id;
    }
}
