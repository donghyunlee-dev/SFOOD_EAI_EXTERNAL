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
public class OrderGoodsId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String companyCode ; // 회사코드
	private String marketMallCode; // 연동몰코드
	private String orderMergeNo; // 주문통합번호
	private Integer orderDetailSeq; // 주문상세번호

	public static OrderGoodsId of(OrderMasterId orderId) {
		OrderGoodsId orderGoodsId = new OrderGoodsId();
		orderGoodsId.companyCode = orderId.getCompanyCode();
		orderGoodsId.marketMallCode = orderId.getMarketMallCode();
		orderGoodsId.orderMergeNo = orderId.getOrderMergeNo();
		return orderGoodsId;
	}
}
