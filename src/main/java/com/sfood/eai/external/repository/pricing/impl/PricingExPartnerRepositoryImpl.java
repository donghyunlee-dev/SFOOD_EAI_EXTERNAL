package com.sfood.eai.external.repository.pricing.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sfood.eai.external.entity.pricing.ExpiryPricingEx;
import com.sfood.eai.external.entity.QGwApproval;
import com.sfood.eai.external.entity.goods.QGoods;
import com.sfood.eai.external.entity.partner.QPartner;
import com.sfood.eai.external.entity.pricing.QPricingExPartner;
import com.sfood.eai.external.repository.pricing.PricingExPartnerRepositoryCustom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class PricingExPartnerRepositoryImpl implements PricingExPartnerRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QPartner partner = QPartner.partner;
    private final QPricingExPartner pricingExPartner = QPricingExPartner.pricingExPartner;
    private final QGwApproval gwApproval = QGwApproval.gwApproval;
    private final QGoods goods = QGoods.goods;

    public PricingExPartnerRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<ExpiryPricingEx> findExpiryPricingExPartner() {

        return queryFactory
                .select(Projections.constructor(ExpiryPricingEx.class
                        , pricingExPartner.id.partnerCode
                        , partner.partnerName
                        , Expressions.constant("거래처별 예외단가")
                        , pricingExPartner.id.goodsCode
                        , goods.goodsName
                        , pricingExPartner.pricingEndDate
                        , gwApproval.approvalId
                        , gwApproval.approvalDate
                        , pricingExPartner.description))
                .from(pricingExPartner)
                .leftJoin(partner)
                .on(pricingExPartner.id.companyCode.eq(partner.companyCode)
                        .and(pricingExPartner.id.partnerCode.eq(partner.partnerCode)))
                .leftJoin(goods)
                .on(pricingExPartner.id.companyCode.eq(goods.companyCode)
                        .and(pricingExPartner.id.goodsCode.eq(goods.goodsCode)))
                .join(gwApproval)
                .on(pricingExPartner.id.companyCode.eq(gwApproval.id.companyCode)
                        .and(pricingExPartner.documentReqNo.eq(gwApproval.id.documentReqNo))
                        .and(gwApproval.approvalStatus.eq("APD")))
                .where(pricingExPartner.pricingEndDate.between(LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(1), LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(7)))
                .fetch();
    }
}
