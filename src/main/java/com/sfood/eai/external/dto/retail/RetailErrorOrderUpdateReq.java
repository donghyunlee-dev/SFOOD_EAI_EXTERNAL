package com.sfood.eai.external.dto.retail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfood.eai.external.dto.CommonReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RetailErrorOrderUpdateReq extends CommonReq {
    @Schema(description = "검색유형")
    @JsonProperty("search_type")
    private String searchType;

    @Schema(description = "검색키워드")
    @JsonProperty("search_keyword")
    private String searchKeyword;

    @Schema(description = "연동 거래처코드")
    @JsonProperty("in_mall_code")
    private String inMallCode;

    @Schema(description = "날짜유형")
    @JsonProperty("date_type")
    private String dateType;

    @Schema(description = "적용시작일자")
    @JsonProperty("start_date")
    private String startDate;

    @Schema(description = "적용종료일자")
    @JsonProperty("end_date")
    private String endDate;

    @Schema(description = "주문처코드")
    @JsonProperty("partner_code")
    private String partnerCode;

    @Schema(description = "상태코드")
    @JsonProperty("status")
    private String status;

    public RetailErrorOrderUpdateReq addParams(HttpServletRequest request) {
        this.setCompanyCode(request.getHeader("company-code"));
        return this;
    }
}
