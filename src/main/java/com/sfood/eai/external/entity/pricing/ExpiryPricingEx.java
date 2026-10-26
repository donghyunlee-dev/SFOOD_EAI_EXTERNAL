package com.sfood.eai.external.entity.pricing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExpiryPricingEx {

    private String partnerCode;

    private String partnerName;

    private String priceType;

    private String goodsCode;

    private String goodsName;

    private LocalDate endDate;

    private String approvalId;

    private String approvalDate;

    private String description;
}
