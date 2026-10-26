package com.sfood.eai.external.repository.retail.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sfood.eai.external.dto.order.ErrorOrderUpdateReq;
import com.sfood.eai.external.entity.order.QOrderDelivery;
import com.sfood.eai.external.entity.order.QOrderGoods;
import com.sfood.eai.external.entity.order.QOrderMaster;
import com.sfood.eai.external.entity.retail.EdiOrderData;
import com.sfood.eai.external.entity.retail.QEdiOrderData;
import com.sfood.eai.external.repository.retail.EdiOrderDataRepositoryCustom;
import com.sfood.eai.external.type.MallOrderType;

import java.util.List;
import java.util.Optional;

public class EdiOrderDataRepositoryCustomImpl implements EdiOrderDataRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QEdiOrderData ediOrderData = QEdiOrderData.ediOrderData;
    private final QOrderMaster orderMaster = QOrderMaster.orderMaster;
    private final QOrderGoods orderGoods = QOrderGoods.orderGoods;
    private final QOrderDelivery orderDelivery = QOrderDelivery.orderDelivery;

    public EdiOrderDataRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<String> getRetailPartners(String startDate, String endDate) {

        try {
            return jpaQueryFactory
                    .select(ediOrderData.ediBpCd)
                    .distinct()
                    .from(ediOrderData)
                    .where(ediOrderData.soDt.between(startDate, endDate))
                    .fetch();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<EdiOrderData> findOrderIdsNullBy(ErrorOrderUpdateReq errorOrderUpdateReq) {

        List<String> orderNos = errorOrderUpdateReq.getErrorOrderList().stream().map(ErrorOrderUpdateReq.ErrorOrderReq::getOrderMergeNo).toList();

        return jpaQueryFactory.selectFrom(ediOrderData)
                .join(orderMaster)
                .on(ediOrderData.mesgNo.eq(orderMaster.marketOrderNo)
                        .and(orderMaster.orderMergeNo.in(orderNos)))
                .leftJoin(orderGoods)
                .on(orderMaster.companyCode.eq(orderGoods.companyCode)
                        .and(orderMaster.marketMallCode.eq(orderGoods.marketMallCode))
                        .and(orderMaster.orderMergeNo.eq(orderGoods.orderMergeNo)))
                .leftJoin(orderDelivery)
                .on(orderGoods.companyCode.eq(orderDelivery.companyCode)
                        .and(orderGoods.marketMallCode.eq(orderDelivery.marketMallCode))
                        .and(orderGoods.orderMergeNo.eq(orderDelivery.orderMergeNo))
                        .and(orderGoods.orderDetailSeq.eq(orderDelivery.orderDetailSeq)))
                .where(orderMaster.companyCode.eq(errorOrderUpdateReq.getCompanyCode())
                        .and(orderMaster.marketMallCode.eq(MallOrderType.RETAIL.code()))
                        .and(orderMaster.orderPartnerCode.isNull()
                                .or(orderMaster.orderPartnerCode.length().lt(6))
                                .or(orderGoods.orderGoodsCode.isNull()))
                )
                .fetch();
    }

    @Override
    public List<EdiOrderData> findOrderByPartnerGroupKey(EdiOrderData.BranchGroupKey groupKey) {

        return jpaQueryFactory
                .select(ediOrderData)
                .from(ediOrderData)
                .where(ediOrderData.companyCode.eq(groupKey.companyCode())
                        .and(ediOrderData.ediType.eq(groupKey.ediType()))
                        .and(ediOrderData.mesgFrom.eq(groupKey.mesgFrom()))
                        .and(ediOrderData.mesgId.eq(groupKey.mesgId()))
                        .and(ediOrderData.mesgName.eq(groupKey.mesgName()))
                        .and(ediOrderData.mesgNo.eq(groupKey.mesgNo()))
                        .and(ediOrderData.mesgTo.eq(groupKey.mesgTo()))
                        .and(ediOrderData.soDt.eq(groupKey.soDt()))
                        .and(ediOrderData.custBpCd.eq(groupKey.custBpCode())))
                .fetch();
    }
}
