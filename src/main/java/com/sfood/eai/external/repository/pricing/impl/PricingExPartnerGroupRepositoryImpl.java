package com.sfood.eai.external.repository.pricing.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sfood.eai.external.entity.pricing.ExpiryPricingEx;
import com.sfood.eai.external.entity.QGwApproval;
import com.sfood.eai.external.entity.goods.QGoods;
import com.sfood.eai.external.entity.pricing.QPartnerGroup;
import com.sfood.eai.external.entity.pricing.QPricingExPartnerGroup;
import com.sfood.eai.external.repository.pricing.PricingExPartnerGroupRepositoryCustom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class PricingExPartnerGroupRepositoryImpl implements PricingExPartnerGroupRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QPartnerGroup partnerGroup = QPartnerGroup.partnerGroup;
    private final QPricingExPartnerGroup pricingExPartnerGroup = QPricingExPartnerGroup.pricingExPartnerGroup;
    private final QGwApproval gwApproval = QGwApproval.gwApproval;
    private final QGoods goods = QGoods.goods;


    public PricingExPartnerGroupRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

   @Override
    public List<ExpiryPricingEx> findExpiryPricingExPartnerGroup() {

        return queryFactory
                .select(Projections.constructor(ExpiryPricingEx.class
                        , pricingExPartnerGroup.id.partnerGroupCode
                        , partnerGroup.partnerGroupName
                        , Expressions.constant("거래처 그룹별 예외단가")
                        , pricingExPartnerGroup.id.goodsCode
                        , goods.goodsName
                        , pricingExPartnerGroup.pricingEndDate
                        , gwApproval.approvalId
                        , gwApproval.approvalDate
                        , pricingExPartnerGroup.description))
                .from(pricingExPartnerGroup)
                .leftJoin(partnerGroup)
                .on(pricingExPartnerGroup.id.companyCode.eq(partnerGroup.id.companyCode)
                        .and(pricingExPartnerGroup.id.partnerGroupCode.eq(partnerGroup.id.partnerGroupCode)))
                .leftJoin(goods)
                .on(pricingExPartnerGroup.id.companyCode.eq(goods.companyCode)
                        .and(pricingExPartnerGroup.id.goodsCode.eq(goods.goodsCode)))
                .join(gwApproval)
                .on(pricingExPartnerGroup.id.companyCode.eq(gwApproval.id.companyCode)
                        .and(pricingExPartnerGroup.documentReqNo.eq(gwApproval.id.documentReqNo))
                        .and(gwApproval.approvalStatus.eq("APD")))
                .where(pricingExPartnerGroup.pricingEndDate.between(LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(1), LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(7)))
                .fetch();
    }
}
