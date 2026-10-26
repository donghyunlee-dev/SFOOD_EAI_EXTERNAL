package com.sfood.eai.external.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Oracle Function 호출 후 Interface 방식으로 데이터 처리
 */
public interface PartnerGoodsPrice {

    String getCompanyCode();

    String getPartnerCode();

    String getPartnerGroupCode();

    String getGoodsCode();

    LocalDate getPricingStartDate();

    LocalDate getPricingEndDate();

    String getSalesUnit();

    BigDecimal getStandardSupplyValue();

    BigDecimal getSupplyValue();

    String getDescription();

    Integer getRevisionNo();

    String getPricingType();
}
