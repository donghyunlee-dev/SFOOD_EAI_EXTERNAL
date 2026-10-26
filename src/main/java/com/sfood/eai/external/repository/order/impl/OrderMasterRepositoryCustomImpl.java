package com.sfood.eai.external.repository.order.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sfood.eai.external.entity.order.QOrderDelivery;
import com.sfood.eai.external.entity.order.QOrderGoods;
import com.sfood.eai.external.entity.order.QOrderMaster;
import com.sfood.eai.external.repository.order.OrderMasterRepositoryCustom;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderMasterRepositoryCustomImpl implements OrderMasterRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QOrderMaster orderMaster = QOrderMaster.orderMaster;
    private final QOrderGoods orderGoods = QOrderGoods.orderGoods;
    private final QOrderDelivery orderDelivery = QOrderDelivery.orderDelivery;

    public OrderMasterRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


}
