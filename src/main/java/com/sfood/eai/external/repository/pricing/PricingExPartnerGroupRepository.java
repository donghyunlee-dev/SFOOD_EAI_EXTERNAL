package com.sfood.eai.external.repository.pricing;

import com.sfood.eai.external.entity.id.pricing.PricingExPartnerGroupId;
import com.sfood.eai.external.entity.pricing.PricingExPartnerGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricingExPartnerGroupRepository extends JpaRepository<PricingExPartnerGroup, PricingExPartnerGroupId>, PricingExPartnerGroupRepositoryCustom {
}
