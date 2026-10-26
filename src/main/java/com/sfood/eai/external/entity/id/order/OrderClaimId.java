package com.sfood.eai.external.entity.id.order;

import com.sfood.eai.external.entity.order.OrderGoods;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderClaimId implements Serializable {

    public static final long serialVersionUID = 1L;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "MARKET_MALL_CODE")
    private String marketMallCode;

    @Setter
    @Column(name = "ORDER_MERGE_NO")
    private String orderMergeNo;

    @Column(name = "ORDER_DETAIL_SEQ")
    private Integer orderDetailSeq;

    public static OrderClaimId newId(OrderGoods orderGoods) {
        return new OrderClaimId(
                orderGoods.getCompanyCode(),
                orderGoods.getMarketMallCode(),
                orderGoods.getOrderMergeNo(),
                orderGoods.getOrderDetailSeq()
        );
    }
}
