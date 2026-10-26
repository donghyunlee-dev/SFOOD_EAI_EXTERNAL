package com.sfood.eai.external.type.godo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GodoDateType {
    ORDER("order", "주문일")
    ,MODIFY("modify", "수정일")
    ;

    private String code;
    private String description;

    public String code() {
        return code;
    }

    public String desc() {
        return description;
    }
}
