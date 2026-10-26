package com.sfood.eai.external.type.godo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GodoMallShopNo {
    ONE("1", "기준몰(국내몰)"),
    TWO("2",	"영문몰"),
    THREE("3",	"중문몰"),
    FOUR("4",	"일문몰")
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
