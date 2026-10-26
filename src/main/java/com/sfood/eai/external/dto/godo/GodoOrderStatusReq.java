package com.sfood.eai.external.dto.godo;

import com.sfood.eai.external.dto.CommonSearchReq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GodoOrderStatusReq extends CommonSearchReq {
	private String orderNo; // 연동주문통합번호
	private String sno; 	// 연동주문상세번호
	private String orderStatus;
	private String invoiceNo;
	private String invoiceCompanySno;
	private String cancelCnt;
	private String handleReason;
	private String returnMethod;
	private String handleDetailReason;

	@Override
	public GodoOrderStatusReq addParams(HttpServletRequest request) {
		this.setCompanyCode(request.getHeader("company-code"));
		this.setRequestUri(request.getRequestURI());
		return this;
	}
}