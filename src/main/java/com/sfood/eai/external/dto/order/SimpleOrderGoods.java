package com.sfood.eai.external.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 주문 품목 간편 정보
 */
@AllArgsConstructor
@Setter
@Getter
public class SimpleOrderGoods {
    private String companyCode;
    private String marketMallCode;
    private String orderMergeNo;
    private Integer orderDetailSeq;
    private String marketOrderNo;
    private String marketOrderSubNo;
    private String orderGoodsCode;
    private String orderGoodsName;
    private String marketGoodsCode;
    private String marketGoodsName;
    private String orderStatusCode;
    private LocalDateTime delvHopeDate;
}
