package com.sfood.eai.external.entity.pricing;

import com.sfood.eai.external.entity.id.pricing.PricingExPartnerId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_PRICING_EX_PARTNER")
@Entity
public class PricingExPartner {

    @EmbeddedId
    private PricingExPartnerId id;

    @Column(name = "PRICING_END_DATE")
    private LocalDate pricingEndDate;

    @Column(name = "SALES_UNIT")
    private String salesUnit;

    @Column(name = "SUPPLY_VALUE")
    private BigDecimal supplyValue;

    @Column(name = "STANDARD_SUPPLY_VALUE")
    private BigDecimal standardSupplyValue;

    @Column(name = "DOCUMENT_REQ_NO")
    private String documentReqNo;

    @Column(name = "EXT1")
    private String ext1;

    @Column(name = "EXT2")
    private String ext2;

    @Column(name = "USE_YN")
    private String useYn;

    @Column(name = "DESCRIPTION")
    private String description;
}
