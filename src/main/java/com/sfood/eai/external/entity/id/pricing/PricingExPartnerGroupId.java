package com.sfood.eai.external.entity.id.pricing;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PricingExPartnerGroupId {
    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "GOODS_CODE")
    private String goodsCode;

    @Column(name = "PARTNER_GROUP_CODE")
    private String partnerGroupCode;

    @Column(name = "PRICING_START_DATE")
    private LocalDate pricingStartDate;

    @Column(name = "REVISION_NO")
    private String revisionNo;
}
