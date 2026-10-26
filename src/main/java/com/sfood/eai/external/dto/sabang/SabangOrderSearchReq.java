package com.sfood.eai.external.dto.sabang;

import com.sfood.eai.external.dto.CommonSearchReq;
import com.sfood.eai.external.type.ChannelType;
import com.sfood.eai.external.type.sabang.SabangOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

@ToString
@Getter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class SabangOrderSearchReq extends CommonSearchReq {

	@Schema(description = "연동몰 회원코드")
	private String mallId;

	@Schema(description = "검색 시작일자")
	private String startDate;

	@Schema(description = "검색 종료 일자")
	private String endDate;

	@Schema(description = "주문번호")
	private String orderNo;

	@Schema(description = "주문 상태")
	private String orderStatus;

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}



	/**
	 * Request 데이터와 검색 일자 초기화
	 *
	 * @param request Http Header, Uri 정보
	 * @return 사방넷 API 호출 기반 검색 조건
	 */
	@Override
    public SabangOrderSearchReq addParams(HttpServletRequest request) {

		this.setCompanyCode(request.getHeader("company-code"));
		this.setUserId(request.getHeader("user-id"));
		this.setRequestUri(request.getRequestURI());
		this.setChannelCode(ChannelType.SABANGNET.code());

		if (!StringUtils.hasText(this.startDate)) {
			this.startDate = getCollectDate();
		}

		if (!StringUtils.hasText(this.endDate)) {
			this.endDate = getCollectDate();
		}

		if (!StringUtils.hasText(this.orderStatus)) {
			this.orderStatus = SabangOrderStatus.S002.code();
		}

		return this;
	}

	public SabangOrderSearchReq setMallId(String mallId) {
		this.mallId = mallId;
		return this;
	}

	public SabangOrderSearchReq setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
		return this;
	}
}
