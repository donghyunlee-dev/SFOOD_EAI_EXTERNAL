package com.sfood.eai.external.dto.sabang;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfood.eai.external.dto.CommonSearchReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class SabangGoodsReq extends CommonSearchReq {

    @Schema(description = "사방넷 품목 리스트")
    @JsonProperty("sabang_goods_info_list")
    private List<SabangGoodsInfo> sabangGoodsInfoList;

    @Override
    public SabangGoodsReq addParams(HttpServletRequest request) {
        this.setCompanyCode(request.getHeader("company-code"));
        this.setUserId(request.getHeader("user-id"));
        this.setRequestUri(request.getRequestURI());
        return this;
    }

    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class SabangGoodsInfo {
        @Schema(description = "품목코드")
        @JsonProperty("goods_code")
        private String goodsCode;

        @Schema(description = "품목명")
        @JsonProperty("goods_name")
        private String goodsName;

        @Schema(description = "제조사")
        @JsonProperty("supplier")
        private String supplier;

        @Schema(description = "원산지")
        @JsonProperty("origin")
        private String origin;

        @Schema(description = "검색키워드")
        @JsonProperty("search")
        private String search;

        @Schema(description = "상태")
        @JsonProperty("status")
        private String status;

        @Schema(description = "판매원가")
        @JsonProperty("cost")
        private Integer cost;

        @Schema(description = "판매단가")
        @JsonProperty("sales_price")
        private Integer salesPrice;

        @Schema(description = "소비자가")
        @JsonProperty("consumer_price")
        private Integer consumerPrice;

        @Schema(description = "부가세 여부")
        @JsonProperty("tax_yn")
        private String taxYn;

        public SabangGoodsInfo(String goodsCode) {
            this.goodsCode = goodsCode;
        }
    }
}
