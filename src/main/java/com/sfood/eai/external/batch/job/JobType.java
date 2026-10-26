package com.sfood.eai.external.batch.job;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobType {

    ORDER_CLOSE("createOrderCloseJob"),
    PRICING_EXCEPTION("sendPricingExMailJob")
    ;

    private final String name;
}
