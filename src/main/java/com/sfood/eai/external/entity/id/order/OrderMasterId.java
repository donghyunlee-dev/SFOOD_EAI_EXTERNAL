package com.sfood.eai.external.entity.id.order;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMasterId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String companyCode ; 	// 회사코드
	private String marketMallCode; 	// 연동몰코드
	private String orderMergeNo ; 	// 주문통합번호
}
