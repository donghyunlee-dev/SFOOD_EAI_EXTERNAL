package com.sfood.eai.external.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfood.eai.external.dto.CommonSearchReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper=false)
public class OrderInvoiceReq extends CommonSearchReq {

    @Schema(description = "연돔몰 송장 리스트")
    @JsonProperty("mall_invoice_list")
    List<MallOrderInvoice> mallInvoiceList;

    @Override
    public OrderInvoiceReq addParams(HttpServletRequest request) {
        this.setCompanyCode(request.getHeader("company-code"));
        this.setUserId(request.getHeader("user-id"));
        this.setRequestUri(request.getRequestURI());
        return this;
    }

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MallOrderInvoice {

        @Schema(description = "송장 정보 리스트")
        @JsonProperty("order_invoice_list")
        List<OrderInvoiceInfo> orderInvoiceList;

        @Schema(description = "연동몰주문번호")
        @JsonProperty("market_mall_code")
        private String marketMallCode;
    }

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class OrderInvoiceInfo {
        @Setter
        @Transient
        private String idx;

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

        @Schema(description = "주문상태코드")
        @JsonProperty("order_status_code")
        private String orderStatusCode;

        @Schema(description = "송장 정보")
        @JsonProperty("invoice_no")
        private String invoiceNo;

        @Schema(description = "송장 전송 일자")
        @JsonProperty("invoice_send_date")
        private String invoiceSendDate;

        @Schema(description = "배송희망일")
        @JsonProperty("hope_delv_date")
        private String hopeDelvDate;
    }
}
