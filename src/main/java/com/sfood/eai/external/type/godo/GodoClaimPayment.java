package com.sfood.eai.external.type.godo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GodoClaimPayment {
    C04003001("04003001", "", "현금환불"),
    C04003002("04003002", "", "PG환불"),
    C04003003("04003003", "", "예치금환불"),
    C04003004("04003004", "", "기타환불"),
    C04003005("04003005", "", "복합환불")
    ;

    private String code;
    private String paymentCode;
    private String description;

    public String code() {
        return code;
    }
}
