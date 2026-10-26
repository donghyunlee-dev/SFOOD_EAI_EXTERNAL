package com.sfood.eai.external.entity.sabang;

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
        name="SABANG_INVOICE_LOG",
        sequenceName = "SABANG_INVOICE_LOG_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Entity
@Table(name="TBL_SABANG_INVOICE_LOG")
public class SabangInvoiceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SABANG_INVOICE_LOG_SEQ")
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

    @Column(name = "IDX")
    private String idx;

    @Column(name = "DELV_CODE")
    private String delvCode;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "HOPE_DELV_DATE")
    private String hopeDelvDate;

    @Column(name = "RESULT")
    private String result;

    @Column(name = "MESSAGE")
    private String message;

    @PrePersist
    public void prePersist() {
        this.logDate = ZonedDateTime.now(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }
}
