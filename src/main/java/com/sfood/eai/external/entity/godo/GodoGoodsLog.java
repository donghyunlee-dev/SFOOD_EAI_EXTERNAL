package com.sfood.eai.external.entity.godo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(
        name="GODO_GOODS_LOG",
        sequenceName = "GODO_GOODS_LOG_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Entity
@Table(name="TBL_GODO_GOODS_LOG")
public class GodoGoodsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GODO_GOODS_LOG_SEQ")
    @Column(name = "SEQ_NO")
    private Long seqNo;

    @CreatedDate
    @Column(name = "LOG_DATE" , updatable = false)
    private LocalDateTime logDate;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "MALL_CODE")
    private String mallCode;

    @Column(name = "GOODS_CODE")
    private String goodsCode;

    @Column(name = "MODE_TYPE")
    private String modeType;

    @Column(name = "GOODS_NO")
    private String goodsNo;

    @Column(name = "RESULT")
    private String result;

    @Column(name = "MESSAGE")
    private String message;
}
