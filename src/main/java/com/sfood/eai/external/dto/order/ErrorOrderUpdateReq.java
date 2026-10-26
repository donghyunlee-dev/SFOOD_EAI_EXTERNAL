package com.sfood.eai.external.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfood.eai.external.dto.CommonReq;
import com.sfood.eai.external.entity.id.order.OrderMasterId;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorOrderUpdateReq extends CommonReq {

    @JsonProperty("error_order_list")
    List<ErrorOrderReq> errorOrderList;

    public ErrorOrderUpdateReq addParams(HttpServletRequest request) {
        this.setCompanyCode(request.getHeader("company-code"));

        if (CollectionUtils.isNotEmpty(errorOrderList)) {
            errorOrderList.stream().forEach(order -> order.setCompanyCode(this.getCompanyCode()));
        }

        return this;
    }

    @Getter
    public static class ErrorOrderReq {
        @Setter
        @JsonProperty("company_code")
        private String companyCode;

        @JsonProperty("mall_code")
        private String mallCode;

        @JsonProperty("order_merge_no")
        private String orderMergeNo;

        public OrderMasterId toOrderMasterId() {
            return new OrderMasterId(this.companyCode, this.mallCode, this.orderMergeNo);
        }
    }
}
