package com.sfood.eai.external.dto.order;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * 주문 품목 금액 누적 합계 계산
 * 주문 총 금액 저장 시 사용
 */
public class OrderGoodsPriceCalculator {

    private BigDecimal salesPrice;
    private BigDecimal supplyPrice;
    private BigDecimal vatPrice;
    @Getter
    private BigDecimal totalSalesPrice;
    @Getter
    private BigDecimal totalSupplyPrice;
    @Getter
    private BigDecimal totalVatPrice;

    public OrderGoodsPriceCalculator() {
        this.salesPrice = BigDecimal.ZERO;
        this.supplyPrice = BigDecimal.ZERO;
        this.vatPrice = BigDecimal.ZERO;
        this.totalSalesPrice = BigDecimal.ZERO;
        this.totalSupplyPrice = BigDecimal.ZERO;
        this.totalVatPrice = BigDecimal.ZERO;
    }

    public void add(BigDecimal salesPrice, BigDecimal supplyPrice, BigDecimal vatPrice) {
        this.salesPrice = salesPrice;
        this.supplyPrice = supplyPrice;
        this.vatPrice = vatPrice;
        sum();
    }

    private void sum() {
        this.totalSalesPrice = this.totalSalesPrice.add(salesPrice);
        this.totalSupplyPrice = this.totalSupplyPrice.add(supplyPrice);
        this.totalVatPrice = this.totalVatPrice.add(vatPrice);
    }
}
