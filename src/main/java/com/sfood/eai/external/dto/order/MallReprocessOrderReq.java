package com.sfood.eai.external.dto.order;

import com.sfood.eai.external.dto.CommonSearchReq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MallReprocessOrderReq extends CommonSearchReq {
    private String orderNo; // 연동주문통합번호

    @Override
    public MallReprocessOrderReq addParams(HttpServletRequest request) {
        this.setCompanyCode(request.getHeader("company-code"));
        return this;
    }
}
