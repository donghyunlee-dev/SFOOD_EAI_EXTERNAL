package com.sfood.eai.external.entity.id.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderSequenceId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name ="COMPANY_CODE")
    private String companyCode;
    @Column(name ="MARKET_MALL_CODE")
    private String marketMallCode;
    @Column(name ="ORDER_DATE")
    private LocalDate orderDate;
}

