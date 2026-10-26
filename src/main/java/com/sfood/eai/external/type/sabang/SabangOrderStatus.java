package com.sfood.eai.external.type.sabang;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SabangOrderStatus {
    S001("OS8030", "신규주문"),
    S002("OS8040", "주문확인"),
    S003("OS2020", "출고대기"),
    S004("004", "출고완료"),
    S006("006", "배송보류"),
    S007("OS3010", "취소접수"),
    S008("OS4010", "교환접수"),
    S009("OS5010", "반품접수"),
    S010("010", "취소완료"),
    S011("011", "교환완료"),
    S012("012", "반품완료"),
    S021("021", "교환발송준비"),
    S022("022", "교환발송완료"),
    S023("023", "교환회수준비"),
    S024("024", "교환회수완료"),
    S025("025", "반품회수준비"),
    S026("026", "반품회수완료"),
    S999("999", "폐기"),
    ;
    private final String value;
    private final String description;


    public String code() {
        return this.name().substring(1,4);
    }

    public String value() {
        return value;
    }

    public String desc() {
        return description;
    }

    public static SabangOrderStatus fromName(String name) {
        for (SabangOrderStatus status : SabangOrderStatus.values()) {
            if (status.description.equals(name)) {
                return status;
            }
        }

        throw new IllegalArgumentException(name + " is not a valid sabang order status");
    }
    public static SabangOrderStatus fromCode(String code) {
        for (SabangOrderStatus status : SabangOrderStatus.values()) {
            if (code.equals(status.code())) {
                return status;
            }
        }

        throw new IllegalArgumentException(code + " is not a valid sabang order status");
    }
}
