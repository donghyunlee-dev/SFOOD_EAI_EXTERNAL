package com.sfood.eai.external.repository.partner;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfood.eai.external.entity.partner.PricingPartner;
import com.sfood.eai.external.entity.id.PricingPartnerId;

@Repository
public interface PricingPartnerRepository extends JpaRepository <PricingPartner, PricingPartnerId> {

	Integer countByCompanyCodeAndPartnerCodeAndGoodsCodeAndPricingStartDateAfter(String companyCode,
			String orderPartnerCode, String orderGoodsCode, Date parse);

}
