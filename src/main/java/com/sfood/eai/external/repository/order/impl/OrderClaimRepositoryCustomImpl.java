package com.sfood.eai.external.repository.order.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sfood.eai.external.entity.order.OrderClaim;
import com.sfood.eai.external.entity.order.QOrderClaim;
import com.sfood.eai.external.entity.order.QOrderGoods;
import com.sfood.eai.external.repository.order.OrderClaimRepositoryCustom;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OrderClaimRepositoryCustomImpl implements OrderClaimRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QOrderGoods orderGoods = QOrderGoods.orderGoods;
    private final QOrderClaim orderClaim = QOrderClaim.orderClaim;

    public OrderClaimRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<OrderClaim> findOrderClaimsByKey(String marketOrderNo, String marketOrderSubNo) {
        return jpaQueryFactory
                .selectFrom(orderClaim)
                .join(orderGoods)
                .on(orderClaim.id.companyCode.eq(orderGoods.companyCode)
                        .and(orderClaim.id.marketMallCode.eq(orderGoods.marketMallCode))
                        .and(orderClaim.id.orderMergeNo.eq(orderGoods.orderMergeNo))
                        .and(orderClaim.id.orderDetailSeq.eq(orderGoods.orderDetailSeq)))
                .where(orderGoods.marketOrderNo.eq(marketOrderNo)
                        .and(orderGoods.marketOrderSubNo.eq(marketOrderSubNo)))
                .fetch();
    }
}
