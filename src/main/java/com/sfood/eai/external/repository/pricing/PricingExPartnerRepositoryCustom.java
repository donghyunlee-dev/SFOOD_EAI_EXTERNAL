package com.sfood.eai.external.repository.pricing;

import com.sfood.eai.external.entity.pricing.ExpiryPricingEx;
import java.util.List;

public interface PricingExPartnerRepositoryCustom {
    List<ExpiryPricingEx> findExpiryPricingExPartner();
}
