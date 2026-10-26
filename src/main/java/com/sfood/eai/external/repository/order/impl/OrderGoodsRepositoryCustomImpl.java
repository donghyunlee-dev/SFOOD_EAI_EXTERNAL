package com.sfood.eai.external.repository.order.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sfood.eai.external.dto.order.OrderCloseStatus;
import com.sfood.eai.external.dto.order.SimpleOrderGoods;
import com.sfood.eai.external.dto.stock.StockMovementCloseReq;
import com.sfood.eai.external.dto.stock.StockMovementClosing;
import com.sfood.eai.external.entity.goods.QGoodsPriceMall;
import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import com.sfood.eai.external.entity.order.QOrderCloseInfo;
import com.sfood.eai.external.entity.order.QOrderDelivery;
import com.sfood.eai.external.entity.order.QOrderGoods;
import com.sfood.eai.external.repository.order.OrderGoodsRepositoryCustom;
import com.sfood.eai.external.type.OrderStatus;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
public class OrderGoodsRepositoryCustomImpl implements OrderGoodsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QOrderGoods orderGoods = QOrderGoods.orderGoods;
    private final QOrderDelivery orderDelivery = QOrderDelivery.orderDelivery;
    private final QOrderCloseInfo orderCloseInfo = QOrderCloseInfo.orderCloseInfo;

    private final LocalDateTime beforeOneWeek = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(2);
    private final List<String> closedStatus = List.of(OrderStatus.OS2040.code(), OrderStatus.OS4040.code(), OrderStatus.OS5040.code());

    public OrderGoodsRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public SimpleOrderGoods findMallOrderGoods(OrderGoodsId orderGoodsId) {
        return jpaQueryFactory
                .select(Projections.constructor(SimpleOrderGoods.class
                        , orderGoods.companyCode
                        , orderGoods.marketMallCode
                        , orderGoods.orderMergeNo
                        , orderGoods.orderDetailSeq
                        , orderGoods.marketOrderNo
                        , orderGoods.marketOrderSubNo
                        , orderGoods.orderGoodsCode
                        , orderGoods.orderGoodsName
                        , orderGoods.marketGoodsCode
                        , orderGoods.marketGoodsName
                        , orderGoods.orderStatusCode
                        , orderDelivery.hopeDelvDate))
                .from(orderGoods)
                .leftJoin(orderDelivery)
                .on(orderGoods.companyCode.eq(orderDelivery.companyCode)
                        .and(orderGoods.orderMergeNo.eq(orderDelivery.orderMergeNo))
                        .and(orderGoods.orderDetailSeq.eq(orderDelivery.orderDetailSeq)))
                .where(orderGoods.companyCode.eq(orderGoodsId.getCompanyCode())
                        .and(orderGoods.marketMallCode.eq(orderGoodsId.getMarketMallCode()))
                        .and(orderGoods.orderMergeNo.eq(orderGoodsId.getOrderMergeNo()))
                        .and(orderGoods.orderDetailSeq.eq(orderGoodsId.getOrderDetailSeq())))
                .fetchOne();
    }

    @Override
    public List<OrderCloseStatus> findClosedOrderGoodsIds() {

        return jpaQueryFactory.select(
                        Projections.constructor(OrderCloseStatus.class
                                , orderGoods.companyCode
                                , orderGoods.marketMallCode
                                , orderGoods.orderMergeNo
                                , orderGoods.orderDetailSeq
                                , orderGoods.orderStatusCode
                        ))
                .from(orderGoods)
                .join(orderDelivery)
                .on(orderGoods.companyCode.eq(orderDelivery.companyCode)
                        .and(orderGoods.marketMallCode.eq(orderDelivery.marketMallCode))
                        .and(orderGoods.orderMergeNo.eq(orderDelivery.orderMergeNo))
                        .and(orderGoods.orderDetailSeq.eq(orderDelivery.orderDetailSeq)))
                .leftJoin(orderCloseInfo)
                .on(orderGoods.companyCode.eq(orderCloseInfo.id.companyCode)
                        .and(orderGoods.orderMergeNo.eq(orderCloseInfo.id.orderMergeNo))
                        .and(orderGoods.orderDetailSeq.eq(orderCloseInfo.id.orderDetailSeq)))
                .where(orderGoods.orderStatusCode.in(closedStatus)
                        .and(orderDelivery.startDelvDate.loe(beforeOneWeek))
                        .and(orderCloseInfo.id.isNull()))
                .fetch();
    }

    @Override
    public List<StockMovementClosing> getStockMovementMaxTransDate() {

        return jpaQueryFactory.select(
                        Projections.constructor(StockMovementClosing.class
                                , orderGoods.companyCode
                                , orderDelivery.startDelvDate.max()
                        ))
                .from(orderGoods)
                .join(orderDelivery)
                .on(orderGoods.companyCode.eq(orderDelivery.companyCode)
                        .and(orderGoods.marketMallCode.eq(orderDelivery.marketMallCode))
                        .and(orderGoods.orderMergeNo.eq(orderDelivery.orderMergeNo))
                        .and(orderGoods.orderDetailSeq.eq(orderDelivery.orderDetailSeq)))
                .leftJoin(orderCloseInfo)
                .on(orderGoods.companyCode.eq(orderCloseInfo.id.companyCode)
                        .and(orderGoods.orderMergeNo.eq(orderCloseInfo.id.orderMergeNo))
                        .and(orderGoods.orderDetailSeq.eq(orderCloseInfo.id.orderDetailSeq)))
                .where(orderGoods.orderStatusCode.in(closedStatus)
                        .and(orderDelivery.startDelvDate.loe(beforeOneWeek))
                        .and(orderCloseInfo.id.isNotNull())
                        .and(orderCloseInfo.closeMethod.eq("B")))
                .groupBy(orderGoods.companyCode)
                .fetch();
    }
}
