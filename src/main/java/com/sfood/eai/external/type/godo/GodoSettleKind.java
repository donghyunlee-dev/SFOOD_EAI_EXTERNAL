package com.sfood.eai.external.type.godo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GodoSettleKind {

    EB("PB", "에스크로 계좌이체"),
    EC("PC", "에스크로 신용카드"),
    EV("PV", "에스크로 가상계좌"),
    FB("PB", "간편결제 계좌이체"),
    FC("PC", "간편결제 신용카드"),
    FH("PC", "간편결제 휴대폰"),
    FP("PC", "간편결제 포인트"),
    FV("PV", "간편결제 가상계좌"),
    FA("GB", "간편결제 무통장입금"),
    GB("GB", "무통장 입금"),
    PB("PB", "계좌이체"),
    PC("PC", "신용카드"),
    PH("PC", "휴대폰"),
    PV("PV", "가상계좌"),
    PK("PC", "간편결제 카카오페이"),
    PL("PC", "간편결제 후불결제"),
    PN("PC", "간편결제 네이버페이"),
    GD("PC", "예치금"),
    GM("PC", "마일리지"),
    GZ("PC", "전액할인"),
    GR("gr", "기타")
    ;

    private String value;
    private String description;

    public String code() {
        return this.name();
    }

    public String value() {
        return this.value;
    }

    public String description() {
        return this.description;
    }
}
