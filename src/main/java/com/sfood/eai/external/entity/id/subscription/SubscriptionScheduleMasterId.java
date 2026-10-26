package com.sfood.eai.external.entity.id.subscription;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionScheduleMasterId implements Serializable {

    public static final long serialVersionUID = 1L;

    private String companyCode;
    private String scheduleCode;
}
