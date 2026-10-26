package com.sfood.eai.external.type.godo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GodoSort {

    DESC("orderNo desc", "주문번호 내림차순"),
    ASC("orderNo asc", "주문번호 오름차순")
    ;

    private String value;
    private String description;

    public String code() {
        return this.name();
    }

    public String value() {return this.value;}

    public String desc() {
        return description;
    }
}
