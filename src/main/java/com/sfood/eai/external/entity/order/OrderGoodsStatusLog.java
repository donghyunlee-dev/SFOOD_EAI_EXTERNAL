package com.sfood.eai.external.entity.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(
        name = "ORDER_GOODS_STATUS_LOG",
        sequenceName = "GOODS_STATUS_LOG_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Table(name = "TBL_ORDER_GOODS_STATUS_LOG")
@Entity
public class OrderGoodsStatusLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GOODS_STATUS_LOG_SEQ")
    @Column(name = "SEQ_NO")
    private Long seqNo;

    @CreatedDate
    @Column(name = "LOG_DATE", updatable = false)
    private LocalDateTime logDate;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "MARKET_MALL_CODE")
    private String marketMallCode;

    @Column(name = "ORDER_MERGE_NO")
    private String orderMergeNo;

    @Column(name = "ORDER_DETAIL_SEQ")
    private Integer orderDetailSeq;

    @Column(name = "PRE_ORDER_STATUS_CODE")
    private String preOrderStatusCode;

    @Column(name = "ORDER_STATUS_CODE")
    private String orderStatusCode;

    @Column(name = "REQ_ID")
    private String reqId;

    @Column(name = "CREATE_ID")
    private String createId;

    @PrePersist
    public void prePersist() {
        this.logDate = ZonedDateTime.now(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }
}
