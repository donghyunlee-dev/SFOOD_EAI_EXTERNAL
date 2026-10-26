package com.sfood.eai.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonReq {

	@Hidden
	@Schema(description = "회사코드",hidden = true)
	private String companyCode;

	@Schema(description = "연동몰코드", nullable = false)
    @JsonProperty("mall_code")
	private String mallCode;
}
