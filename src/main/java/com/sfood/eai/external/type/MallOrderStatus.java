package com.sfood.eai.external.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MallOrderStatus {
    PENDING,
    PLACED,
    MODIFIED,
    CONFIRMED,
    DELIVERED,
    INVOICED,
    CANCELLED;

    public String code() {
        return  this.name();
    }
}
