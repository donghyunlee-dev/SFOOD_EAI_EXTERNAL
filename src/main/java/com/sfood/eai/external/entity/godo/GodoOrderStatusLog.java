package com.sfood.eai.external.entity.godo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(
        name="GODO_ORDER_STATUS_LOG",
        sequenceName = "GODO_ORDER_STATUS_LOG_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Entity
@Table(name="TBL_GODO_ORDER_STATUS_LOG")
public class GodoOrderStatusLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GODO_ORDER_STATUS_LOG_SEQ")
    @Column(name = "SEQ_NO")
    private Long seqNo;

    @CreatedDate
    @Column(name = "LOG_DATE" , updatable = false)
    private LocalDateTime logDate;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "MARKET_MALL_CODE")
    private String marketMallCode;

    @Column(name = "ORDER_MERGE_NO")
    private String orderMergeNo;

    @Column(name = "ORDER_DETAIL_SEQ")
    private Integer orderDetailSeq;

    @Column(name = "CODE")
    private Integer code;

    @Column(name = "MESSAGE")
    private String msg;

    @Column(name = "ORDER_NO")
    private String orderNo;

    @Column(name = "SNO")
    private String sno;

    @Column(name = "ORDER_STATUS")
    private String orderStatus;

    @Column(name = "RES_CODE")
    private String resCode;

    @Column(name = "RES_MESSAGE")
    private String resMsg;

    @PrePersist
    public void prePersist() {
        this.logDate = ZonedDateTime.now(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }
}
