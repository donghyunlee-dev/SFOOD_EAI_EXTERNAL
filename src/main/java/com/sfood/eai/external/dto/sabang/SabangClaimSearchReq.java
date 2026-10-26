package com.sfood.eai.external.dto.sabang;

import com.sfood.eai.external.dto.CommonSearchReq;
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
public class SabangClaimSearchReq extends CommonSearchReq {

	@Schema(description = "검색 시작일자")
	private String startDate;
	@Schema(description = "검색 종료 일자")
	private String endDate;

	@Override
	public Object addParams(HttpServletRequest request) {

		this.setCompanyCode(request.getHeader("company-code"));
		this.setUserId(request.getHeader("user-id"));
		this.setRequestUri(request.getRequestURI());

		if (!StringUtils.hasText(this.startDate)) {
			this.startDate = getCollectDate();
		}

		if (!StringUtils.hasText(this.endDate)) {
			this.endDate = getCollectDate();
		}

		return this;
	}
}
