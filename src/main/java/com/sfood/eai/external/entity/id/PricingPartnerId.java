package com.sfood.eai.external.entity.id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PricingPartnerId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String companyCode ; 	// 회사코드
	private String partnerCode ; 	// 거래처코드
	private String goodsCode ; 		// 품목코드
	private Date pricingStartDate ; // 단가적용시작일
}
