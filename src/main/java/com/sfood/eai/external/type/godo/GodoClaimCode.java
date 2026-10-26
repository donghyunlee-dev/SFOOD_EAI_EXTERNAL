package com.sfood.eai.external.type.godo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GodoClaimCode {
    C04001001("04001001", "고객변심"),
    C04001002("04001002", "품절취소"),
    C04001003("04001003", "배송지연취소"),
    C04001004("04001004", "중복주문취소"),
    C04001005("04001005", "시스템오류취소"),
    C04001006("04001006", "누락배송"),
    C04001007("04001007", "택배분실"),
    C04001008("04001008", "상품불량"),
    C04001009("04001009", "배송불가지역"),
    C04001010("04001010", "포장불량"),
    C04001011("04001011", "상품정보상이"),
    C04001012("04001012", "상품불만족"),
    C04001013("04001013", "서비스불만족"),
    C04001014("04001014", "기타");
    private final String code;
    private final String description;

    public String code() {
        return code;
    }
}
