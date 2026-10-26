package com.sfood.eai.external.type.godo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GodoOrderChannel {
    SHOP("shop", "쇼핑몰 주문"),
    PAYCO("payco", "페이코 주문"),
    NAVERPAY("naverpay", "네이버페이 주문");

    private String code;
    private String description;

    public String code() {
        return code;
    }

    public String desc() {
        return description;
    }
}
