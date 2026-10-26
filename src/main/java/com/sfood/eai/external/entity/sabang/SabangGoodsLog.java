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
        name="SABANG_GOODS_LOG",
        sequenceName = "SABANG_GOODS_LOG_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Entity
@Table(name="TBL_SABANG_GOODS_LOG")
public class SabangGoodsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SABANG_GOODS_LOG_SEQ")
    @Column(name = "SEQ_NO")
    private Long seqNo;

    @CreatedDate
    @Column(name = "LOG_DATE" , updatable = false)
    private LocalDateTime logDate;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "GOODS_CODE")
    private String goodsCode;

    @Column(name = "MODE_TYPE")
    private String modeType;

    @Column(name = "PRODUCT_ID")
    private Integer productId;

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
