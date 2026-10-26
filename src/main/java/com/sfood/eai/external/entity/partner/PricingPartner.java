package com.sfood.eai.external.entity.partner;

import java.util.Date;

import com.sfood.eai.external.entity.id.PricingPartnerId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="TBL_PRICING_PARTNER")
@IdClass(PricingPartnerId.class)
public class PricingPartner {
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode ; // 회사코드

	@Id
	@Column(name = "PARTNER_CODE")
	private String partnerCode ; // 거래처코드
	
	@Id
	@Column(name = "GOODS_CODE")
	private String goodsCode ; // 품목코드
	
	@Id
	@Column(name = "PRICING_START_DATE")
	private Date pricingStartDate ; // 단가적용시작일
}
