package com.sfood.eai.external.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@Schema(description = "CommonData")
@Setter
@Getter
public class CommonDataRes {

	@Schema(description = "성공개수")
	@JsonProperty("success_cnt")
	private Integer successCnt;

	@Schema(description = "실패데이터", nullable = true)
	@JsonProperty("fail_data")
	private List<FailDataInfo> failData;

	@Schema(description = "생성키", nullable = true)
	@JsonProperty("create_key")
	private KeyNameValue createKey;

	public CommonDataRes() {
		this.successCnt = 0;
	}

	public int addSuccessCnt(int successCnt) {
		this.successCnt += successCnt;
		return this.successCnt;
	}

	public static CommonDataRes noDataResponse() {
		return new CommonDataRes();
	}

	public static CommonDataRes getDataResponse(int successCnt) {
		CommonDataRes res = new CommonDataRes();
		res.successCnt = successCnt;
		return res;
	}
}
