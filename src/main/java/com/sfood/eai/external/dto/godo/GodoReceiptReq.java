package com.sfood.eai.external.dto.godo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GodoReceiptReq {

    private String startDate;
    private String endDate;
    private String receiptFl;
}
