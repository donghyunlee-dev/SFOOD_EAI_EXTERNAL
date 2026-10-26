package com.sfood.eai.external.type.godo;

import com.mysema.commons.lang.Assert;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GodoOrderStatus {
    O1("OS8030",  "입금대기", false),
    P1("OS8040", "결제완료", false),
    G1("OS2020", "상품준비중", false),
    G2("g2", "구매발주", false),
    G3("g3", "상품입고", false),
    G4("g4", "상품출고", false),
    D1("OS2030", "배송중", false),
    D2("d2", "배송완료", false),
    S1("OS2040", "구매확정", false),
    C1("OS3030", "자동취소", true),
    C2("OS3030", "품절취소", true),
    C3("OS3030", "관리자취소", true),
    C4("OS3030", "고객취소요청", true),
    F1("f1", "결제시도", false),
    F2("f2", "고객결제중단", false),
    F3("f3", "결제실패", false),
    F4("f4", "?????", false),
    B1("OS5010", "반품접수", true),
    B2("b2", "반송중", false),
    B3("OS5030", "반품보류", false),
    B4("OS5020", "반품회수완료", true),
    E1("OS4020", "교환접수", true),
    E2("e2", "반송중", false),
    E3("e3", "재배송중", false),
    E4("OS4030", "교환보류", false),
    E5("OS4020", "교환완료", true),
    R1("OS6020", "환불접수", true),
    R2("OS6030", "환불보류", false),
    R3("OS6040", "환불완료", true),
    Z1("OS8030", "추가입금대기", false),
    Z2("OS8040", "추가결제완료", false),
    Z3("OS2030", "추가배송중", false),
    Z4("OS2040", "추가배송완료", false),
    Z5("OS4040", "교환추가완료", false)
    ;
    private final String value;
    private final String description;
    private final boolean isClaim;


    public String code() {
        return this.name().toLowerCase();
    }

    public String value() {
        return this.value;
    }

    public String desc() {
        return this.description;
    }

    public Boolean isClaimStatus() {
        return this.isClaim;
    }

    public static String getValue(String name) {
        return GodoOrderStatus.valueOf(name.toUpperCase()).value();
    }

    public static GodoOrderStatus fromValue(String value) {

        Assert.notNull(value, "value is not null");

        for(GodoOrderStatus status : GodoOrderStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException(value + " is not a valid godo order status");
    }

    public static GodoOrderStatus fromCode(String code) {

        Assert.notNull(code, "code is not null");

        for(GodoOrderStatus status : GodoOrderStatus.values()) {
            if (code.equals(status.code())) {
                return status;
            }
        }

        throw new IllegalArgumentException(code + " is not a valid godo order status");
    }
}
