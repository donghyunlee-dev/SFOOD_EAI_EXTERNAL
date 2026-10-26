package com.sfood.eai.external.repository.godo.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sfood.eai.external.dto.godo.GodoOrderSearchReq;
import com.sfood.eai.external.dto.order.MallOrderId;
import com.sfood.eai.external.entity.godo.QGodoOrder;
import com.sfood.eai.external.entity.order.QOrderGoods;
import com.sfood.eai.external.repository.godo.GodoOrderRepositoryCustom;
import com.sfood.eai.external.type.OrderStatus;
import com.sfood.eai.external.type.godo.GodoOrderStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GodoOrderRepositoryCustomImpl implements GodoOrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QGodoOrder godoOrder = QGodoOrder.godoOrder;
    private final QOrderGoods orderGoods = QOrderGoods.orderGoods;

    public GodoOrderRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<MallOrderId> findUnsettledOrderIds(GodoOrderSearchReq searchReq) {
        try {
            return jpaQueryFactory
                    .select(Projections.constructor(MallOrderId.class
                            , godoOrder.companyCode
                            , godoOrder.marketMallCode
                            , godoOrder.orderNo
                    ))
                    .distinct()
                    .from(godoOrder)
                    .join(orderGoods)
                    .on(godoOrder.companyCode.eq(orderGoods.companyCode)
                            .and(godoOrder.marketMallCode.eq(orderGoods.marketMallCode)
                                    .and(godoOrder.orderNo.eq(orderGoods.marketOrderNo))
                                    .and(orderGoods.orderStatusCode.eq(OrderStatus.OS1020.code())))
                    ).where(godoOrder.companyCode.eq(searchReq.getCompanyCode())
                            .and(godoOrder.marketMallCode.eq(searchReq.getMallCode()))
                            .and(godoOrder.orderStatus.eq(GodoOrderStatus.O1.code()))
                    ).fetch();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
