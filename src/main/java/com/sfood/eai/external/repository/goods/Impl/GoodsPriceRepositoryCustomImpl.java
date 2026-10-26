package com.sfood.eai.external.repository.goods.Impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sfood.eai.external.entity.goods.GoodsPrice;
import com.sfood.eai.external.entity.goods.QGoods;
import com.sfood.eai.external.entity.goods.QPricingMaster;
import com.sfood.eai.external.repository.goods.GoodsPriceRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class GoodsPriceRepositoryCustomImpl implements GoodsPriceRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QGoods goods = QGoods.goods;
    private final QPricingMaster price = QPricingMaster.pricingMaster;

    public GoodsPriceRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<GoodsPrice> findGoodsPrice(String companyCode, String goodsCode) {
        try {

            Tuple latestPricing = jpaQueryFactory
                    .select(price.pricingStartDate, price.pricingIdx)
                    .from(price)
                    .where(price.companyCode.eq(companyCode)
                            .and(price.goodsCode.eq(goodsCode))
                            .and(price.pricingStartDate.loe(LocalDateTime.now()))
                            .and(price.approveStatus.eq("A"))
                            .and(price.useYn.eq("Y"))
                    )
                    .orderBy(price.pricingStartDate.desc(), price.pricingIdx.desc())
                    .fetchFirst();

            if (ObjectUtils.isEmpty(latestPricing)) {
                return null;
            }

            LocalDateTime latestPricingStartDate = latestPricing != null ? latestPricing.get(price.pricingStartDate) : null;
            Integer latestPricingIdx = latestPricing != null ? latestPricing.get(price.pricingIdx) : null;

            return jpaQueryFactory
                    .select(Projections.constructor(GoodsPrice.class,
                            goods.companyCode,
                            goods.goodsCode,
                            goods.bundleYn,
                            goods.erpGoodsCode,
                            goods.goodsName,
                            goods.exclusiveYn,
                            goods.salesStatus,
                            price.supplyValue,
                            price.vatValue,
                            goods.taxYn,
                            goods.category1,
                            goods.category2,
                            goods.category3,
                            goods.category4,
                            goods.optionYn,
                            goods.optionType,
                            goods.optionTitle,
                            goods.wmsApiYn,
                            goods.warehouseCode,
                            goods.originCode,
                            goods.supplierCode,
                            goods.goodsUnit,
                            goods.barcode,
                            goods.goodsTemperature,
                            goods.subscriptionType,
                            goods.expirationMonth,
                            goods.expirationDay,
                            goods.useYn,
                            goods.goodsDescription,
                            goods.adminMemo,
                            goods.goodsNameShort,
                            goods.goodsKeywords
                    ))
                    .from(goods)
                    .leftJoin(price)
                    .on(goods.companyCode.eq(price.companyCode)
                            .and(goods.goodsCode.eq(price.goodsCode))
                            .and(price.pricingStartDate.eq(latestPricingStartDate))
                            .and(price.pricingIdx.eq(latestPricingIdx)))
                    .where(goods.companyCode.eq(companyCode)
                            .and(goods.goodsCode.eq(goodsCode)))
                    .fetch();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
