package com.sfood.eai.external.type.godo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GodoSearchType {

    ORDER("orderPhone",	"주문자 전화번호"),
    RECEIVER("receiverPhone",	"수령자 전화번호"),
    ORDERCELL("orderCellPhone",	"주문자 휴대폰번호"),
    RECEIVERCELL("receiverCellPhone",	"수령자 휴대폰번호");

    private String code;
    private String description;

    public String code() {
        return code;
    }

    public String desc() {
        return description;
    }
}
