    package com.sfood.eai.external.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfood.eai.external.dto.CommonSearchReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper=false)
public class OrderStatusReq extends CommonSearchReq {

    @Schema(description = "연돔몰 주문 리스트")
    @JsonProperty("mall_order_list")
    List<MallOrderStatus> mallOrderList;

    @Override
    public OrderStatusReq addParams(HttpServletRequest request) {
        this.setCompanyCode(request.getHeader("company-code"));
        this.setUserId(request.getHeader("user-id"));
        this.setRequestUri(request.getRequestURI());
        return this;
    }

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MallOrderStatus {

        @Schema(description = "주문 상태 리스트")
        @JsonProperty("order_status_list")
        List<OrderStatusInfo> orderstatusList;

        @Schema(description = "연동몰주문번호")
        @JsonProperty("market_mall_code")
        private String marketMallCode;
    }

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class OrderStatusInfo {
        @Schema(description = "주문번호")
        @JsonProperty("order_merge_no")
        private String orderMergeNo;

        @Schema(description = "주문상세번호")
        @JsonProperty("order_detail_seq")
        private Integer orderDetailSeq;

        @Schema(description = "연동몰주문번호")
        @JsonProperty("market_order_no")
        private String marketOrderNo;

        @Schema(description = "연동몰상세번호")
        @JsonProperty("market_order_sub_no")
        private String marketOrderSubNo;

        @Schema(description = "상태코드")
        @JsonProperty("order_status_code")
        private String orderStatusCode;
    }
}
