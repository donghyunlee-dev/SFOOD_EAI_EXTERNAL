package com.sfood.eai.external.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    OS1010("입금대기"),
    OS1020("신규주문"),
    OS1030("주문확정"),
    OS2010("출고요청"),
    OS2020("출고준비중"),
    OS2030("출하대기"),
    OS2040("출고완료"),
    OS3010("취소요청"),
    OS3020("취소승인"),
    OS3030("취소완료"),
    OS4010("교환요청"),
    OS4020("교환승인"),
    OS4030("교환불가"),
    OS4040("교환완료"),
    OS4050("교환진행중"),
    OS5010("반품요청"),
    OS5020("반품승인"),
    OS5030("반품불가"),
    OS6010("환불요청"),
    OS5040("반품완료"),
    OS5050("반품진행중"),
    OS6020("환불승인"),
    OS6030("환불불가"),
    OS6040("환불완료"),
    OS9999("미매칭주문"),
    OS8010("신규주문(오류)"),
    OS8020("주문확정(오류)"),
    OS8030("신규주문(대기)"),
    OS8040("주문확정(대기)")
    ;

    private final String description;

    public String code() {
        return name();
    }

    public int codeToInt() {
        return Integer.parseInt(name().substring(2));
    }

    public String desc() {
        return description;
    }

    public boolean equals(String code) {
        return OrderStatus.valueOf(code).equals(this);
    }
}
