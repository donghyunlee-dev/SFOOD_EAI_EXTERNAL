package com.sfood.eai.external.dto.order;

import com.sfood.eai.external.type.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCloseStatus implements Serializable {

	private String companyCode ;
	private String marketMallCode;
	private String orderMergeNo;
	private Integer orderDetailSeq;
	private String orderStatusCode;

	public String getOrderType() {
		return OrderStatus.OS2040.equals(orderStatusCode) ? "O" : "C";
	}
}
