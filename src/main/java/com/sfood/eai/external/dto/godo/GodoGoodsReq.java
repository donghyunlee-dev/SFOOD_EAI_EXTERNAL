package com.sfood.eai.external.dto.godo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfood.eai.external.dto.CommonSearchReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GodoGoodsReq extends CommonSearchReq {

    @Schema(description = "고도몰 품목 리스트")
    @JsonProperty("godo_goods_info_list")
    private List<GodoGoodsInfo> godoGoodsInfoList;

    @Override
    public GodoGoodsReq addParams(HttpServletRequest request) {
        this.setCompanyCode(request.getHeader("company-code"));
        this.setUserId(request.getHeader("user-id"));
        this.setRequestUri(request.getRequestURI());
        return this;
    }

    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class GodoGoodsInfo {
        @Setter
        @Transient
        private String idx;

        @Schema(description = "품목코드")
        @JsonProperty("goods_code")
        private String goodsCd;

        @Schema(description = "품목명")
        @JsonProperty("goods_name")
        private String goodsNm;

        @Setter
        @Schema(description = "연동몰코드")
        @JsonProperty("market_goods_code")
        private String goodsNo;

        @Schema(description = "검색어")
        @JsonProperty("search")
        private String search;

        @Schema(description = "제조사")
        @JsonProperty("supplier")
        private String supplier;

        @Schema(description = "원산지")
        @JsonProperty("origin")
        private String origin;

        @Schema(description = "정가")
        @JsonProperty("fixed_price")
        private Integer fixedPrice;

        @Schema(description = "매입가")
        @JsonProperty("cost")
        private Integer cost;

        @Schema(description = "판매단가")
        @JsonProperty("sales_price")
        private Integer salesPrice;

        @Schema(description = "PC쇼핑몰 노출상태")
        @JsonProperty("goods_display_fl")
        private String goodsDisplayFl;

        @Schema(description = "모바일쇼핑몰 노출상태")
        @JsonProperty("goods_display_mobile_fl")
        private String goodsDisplayMobileFl;

        @Schema(description = "PC쇼핑몰 판매상태")
        @JsonProperty("goods_sell_fl")
        private String goodsSellFl;

        @Schema(description = "모바일쇼핑몰 판매상태")
        @JsonProperty("goods_sell_mobile_fl")
        private String goodsSellMobileFl;

        @Schema(description = "과세여부")
        @JsonProperty("tax_yn")
        private String taxYn;
    }
}
