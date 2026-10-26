package com.sfood.eai.external.entity.id.godo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GodoOrderId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String companyCode; // 회사코드
	private String orderChannelCode; // 주문채널코드(주문 데이터 정의.xlsx : 주문채널)
	private String marketMallCode; // 연동몰코드(주문 데이터 정의.xlsx : 쇼핑몰명)
	private String orderNo ; // 주문통합번호
}
