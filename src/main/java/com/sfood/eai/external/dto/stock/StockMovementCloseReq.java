package com.sfood.eai.external.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockMovementCloseReq {

    @JsonProperty("c_company_code")
    private String companyCode;

    @JsonProperty("max_date")
    private String maxDate;

    @JsonProperty("user_id")
    private String userId;
}
