package com.sfood.eai.external.repository.goods.Impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sfood.eai.external.entity.goods.*;
import com.sfood.eai.external.entity.id.goods.GoodsId;
import com.sfood.eai.external.repository.goods.GoodsRepositoryCustom;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
public class GoodsRepositoryCustomImpl implements GoodsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QGoods goods = QGoods.goods;
    private final QErpGoods erpGoods = QErpGoods.erpGoods;
    private final QGoodsPriceMall priceMall = QGoodsPriceMall.goodsPriceMall;

    public GoodsRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<SimpleGoods> getGoodsByMall(String companyCode, String mallCode, String goodsCode) {
        try {

            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul")).withHour(0).withMinute(0).withSecond(0).withNano(0);
            QGoodsPriceMall subQuery = new QGoodsPriceMall("subQuery");

            JPQLQuery<Integer> maxRevisionNoSubQuery = JPAExpressions
                    .select(subQuery.revisionNo.max())
                    .from(subQuery)
                    .where(subQuery.companyCode.eq(companyCode)
                            .and(subQuery.mallCode.eq(mallCode))
                            .and(subQuery.goodsCode.eq(goodsCode))
                            .and(subQuery.pricingStartDate.loe(now).and(subQuery.pricingEndDate.goe(now)))
                            .and(subQuery.useYn.eq("Y"))
                    );

            return jpaQueryFactory
                    .select(Projections.constructor(SimpleGoods.class
                            , goods.goodsCode
                            , goods.goodsName
                            , goods.erpGoodsCode
                            , erpGoods.erpGoodsName
                            , goods.wmsApiYn
                            , goods.goodsUnit
                            , goods.warehouseCode
                            , goods.bundleYn
                            , goods.barcode
                            , goods.goodsTemperature
                            , goods.subscriptionType
                            , goods.taxYn
                            , Expressions.numberTemplate(Integer.class, "FN_GET_INV_AV_QTY({0}, {1}, {2})", goods.companyCode, goods.warehouseCode, goods.erpGoodsCode == null ? goods.goodsCode : goods.erpGoodsCode)
                    )).from(goods)
                    .leftJoin(erpGoods)
                    .on(goods.companyCode.eq(erpGoods.companyCode)
                            .and(goods.erpGoodsCode.eq(erpGoods.erpGoodsCode)))
                    .join(priceMall)
                    .on(goods.companyCode.eq(priceMall.companyCode)
                            .and(goods.goodsCode.eq(priceMall.goodsCode))
                            .and(priceMall.mallCode.eq(mallCode))
                            .and(priceMall.pricingStartDate.loe(now).and(priceMall.pricingEndDate.goe(now)))
                            .and(priceMall.useYn.eq("Y"))
                            .and(priceMall.revisionNo.eq(maxRevisionNoSubQuery))
                    )
                    .where(goods.companyCode.eq(companyCode)
                            .and(goods.goodsCode.eq(goodsCode))) // 변경 ->
                    .fetch();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw (e);
        }
    }

    @Override
    public SimpleGoods getSimpleGoods(GoodsId goodsId) {
        try {
            return jpaQueryFactory
                    .select(Projections.constructor(SimpleGoods.class
                            , goods.goodsCode
                            , goods.goodsName
                            , goods.erpGoodsCode
                            , erpGoods.erpGoodsName
                            , goods.wmsApiYn
                            , goods.goodsUnit
                            , goods.warehouseCode
                            , goods.bundleYn
                            , goods.barcode
                            , goods.goodsTemperature
                            , goods.subscriptionType
                            , goods.taxYn
                            , Expressions.numberTemplate(Integer.class, "FN_GET_INV_AV_QTY({0}, {1}, {2})", goods.companyCode, goods.warehouseCode, goods.erpGoodsCode == null ? goods.goodsCode : goods.erpGoodsCode)
                    )).from(goods)
                    .leftJoin(erpGoods)
                    .on(goods.companyCode.eq(erpGoods.companyCode)
                            .and(goods.erpGoodsCode.eq(erpGoods.erpGoodsCode)))
                    .where(goods.companyCode.eq(goodsId.getCompanyCode())
                            .and(goods.goodsCode.eq(goodsId.getGoodsCode())))
                    .fetchOne();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw (e);
        }
    }

}
