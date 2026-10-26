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
public class OrderAddGoodsId implements Serializable {

	public static final long serialVersionUID = 1L;

	private String companyCode ; // 회사코드
	private String marketMallCode; // 연동몰코드
	private String orderMergeNo; // 주문통합번호
	private Integer orderDetailSeq; // 주문상세번호

	public static OrderAddGoodsId of(OrderMasterId orderId) {
		OrderAddGoodsId orderAddGoodsId = new OrderAddGoodsId();
		orderAddGoodsId.companyCode = orderId.getCompanyCode();
		orderAddGoodsId.marketMallCode = orderId.getMarketMallCode();
		orderAddGoodsId.orderMergeNo = orderId.getOrderMergeNo();
		return orderAddGoodsId;
	}
}
