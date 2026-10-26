package com.sfood.eai.external.entity.goods;

import com.sfood.eai.external.entity.id.goods.GoodsPriceMallId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="TBL_PRICING_MALL")
@IdClass(GoodsPriceMallId.class)
public class GoodsPriceMall {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Id
    @Column(name = "MALL_CODE")
    private String mallCode;

    @Id
    @Column(name = "GOODS_CODE")
    private String goodsCode;

    @Id
    @Column(name = "PRICING_START_DATE")
    private LocalDateTime pricingStartDate;

    @Column(name = "PRICING_END_DATE")
    private LocalDateTime pricingEndDate;

    @Id
    @Column(name = "REVISION_NO")
    private Integer revisionNo;

    @Column(name = "SALES_UNIT")
    private String salesUnit;

    @Column(name = "SUPPLY_VALUE")
    private BigDecimal supplyValue;

    @Column(name = "VAT_VALUE")
    private BigDecimal vatValue;

    @Column(name = "SALE_VALUE")
    private BigDecimal saleValue;

    @Column(name = "USE_YN")
    private String useYn;
}
