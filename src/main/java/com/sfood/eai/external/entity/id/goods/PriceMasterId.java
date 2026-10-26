package com.sfood.eai.external.entity.id.goods;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PriceMasterId implements Serializable {

    public static final long serialVersionUID = 1L;

    private String companyCode;
    private String goodsCode;
    private LocalDateTime pricingStartDate;
    private Integer pricingIdx;
}
