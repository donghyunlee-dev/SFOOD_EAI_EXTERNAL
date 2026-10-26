package com.sfood.eai.external.repository.pricing;

import com.sfood.eai.external.entity.id.pricing.PricingExPartnerId;
import com.sfood.eai.external.entity.pricing.PricingExPartner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricingExPartnerRepository extends JpaRepository<PricingExPartner, PricingExPartnerId>, PricingExPartnerRepositoryCustom {
}
