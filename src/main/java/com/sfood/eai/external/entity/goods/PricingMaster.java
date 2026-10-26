package com.sfood.eai.external.entity.goods;


import com.sfood.eai.external.entity.id.goods.PriceMasterId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@NamedEntityGraph
@Entity
@Table(name="TBL_PRICING_MST")
@IdClass(PriceMasterId.class)
public class PricingMaster {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Id
    @Column(name = "GOODS_CODE")
    private String goodsCode;

    @Id
    @Column(name = "PRICING_START_DATE")
    private LocalDateTime pricingStartDate;

    @Id
    @Column(name = "PRICING_IDX")
    private Integer pricingIdx;

    @Column(name = "SUPPLY_VALUE")
    private BigDecimal supplyValue;

    @Column(name = "VAT_VALUE")
    private BigDecimal vatValue;

    @Column(name = "APPROVE_STATUS")
    private String approveStatus;

    @Column(name = "USE_YN")
    private String useYn;
}
