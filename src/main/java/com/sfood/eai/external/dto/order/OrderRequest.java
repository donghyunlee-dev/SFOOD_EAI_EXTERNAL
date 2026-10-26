package com.sfood.eai.external.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfood.eai.external.dto.CommonReq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderRequest extends CommonReq {
    @JsonProperty("order_date")
    private String orderDate;

    public OrderRequest addParams(HttpServletRequest request) {
        this.setCompanyCode(request.getHeader("company-code"));
        return this;
    }
}
