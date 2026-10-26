package com.sfood.eai.external.entity.id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MarketMallMasterId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String companyCode; 	// 법인코드
	private String channelCode; 	// 채널코드 - 코드테이블에 채널명존재
	private String mallCode; 		// 연동몰코드

}
