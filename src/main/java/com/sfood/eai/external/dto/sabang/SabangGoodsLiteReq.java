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
public class SabangGoodsLiteReq extends CommonSearchReq {

    @Schema(description = "사방넷 품목 리스트")
    @JsonProperty("sabang_goods_info_list")
    private List<SabangGoodsInfo> sabangGoodsInfoList;

    @Override
    public SabangGoodsLiteReq addParams(HttpServletRequest request) {
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

        @Schema(description = "상태")
        @JsonProperty("status")
        private String status;

        @Schema(description = "판매단가")
        @JsonProperty("sales_price")
        private Integer salesPrice;

        @Schema(description = "옵션")
        @JsonProperty("sku_info")
        private List<String> skuInfo;
    }
}
