package com.sfood.eai.external.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CommonSearchReq {
	
	@Schema(description = "회사코드",hidden = true)
	private String companyCode;
	
	@Schema(description = "채널코드")
	private String channelCode;
	
	@Schema(description = "연동몰코드")
	private String mallCode;

	@Transient
	@Schema(description = "최초 요청 rest request uri(controller)", hidden = true)
	private String requestUri;

	@Schema(description = "검색일자(from to가 존재하나 속도 이슈로 당일 주문 검색으로 제한한다.")
	private String collectDate;

	@Transient
	@Schema(description = "사용자ID", hidden = true)
	private String userId;

	public abstract Object addParams(HttpServletRequest request);

	public void copyFrom(CommonSearchReq req) {
		this.companyCode = req.companyCode;
		this.channelCode = req.channelCode;
		this.mallCode = req.mallCode;
		this.requestUri = req.requestUri;
		this.collectDate = req.collectDate;
		this.userId = req.userId;
	}
}
