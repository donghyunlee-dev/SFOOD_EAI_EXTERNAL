package com.sfood.eai.external.mapper.order;


import com.sfood.eai.external.entity.godo.GodoAddGoods;
import com.sfood.eai.external.entity.order.OrderAddGoods;
import com.sfood.eai.external.entity.order.OrderMaster;
import com.sfood.eai.external.type.godo.GodoOrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderAddGoodsMapper {

    OrderAddGoodsMapper INSTANCE = Mappers.getMapper(OrderAddGoodsMapper.class);

    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "orderDetailSeq", source = "orderMaster.maxOrderDetailSeq")
    @Mapping(target = "marketOrderSubNo", source = "godoAddGoods.sno")
    @Mapping(target = "orderStatusCode", source = "godoAddGoods.orderStatus", qualifiedByName = "getGodoStatus")
    @Mapping(target = "soTypeCode", constant = "ZSOA")
    @Mapping(target = "orderGoodsCode", source = "godoAddGoods.goodsCd")
    @Mapping(target = "marketGoodsCode", source = "godoAddGoods.addGoodsNo")
    @Mapping(target = "marketGoodsName", source = "godoAddGoods.goodsNm")
    @Mapping(target = "orderCnt", constant = "1")
    @Mapping(target = "realDeliveryCnt", constant = "0")
    @Mapping(target = "issueDeliveryCnt", constant = "0")
    @Mapping(target = "returnRealYn", constant = "N")
    @Mapping(target = "salesOrderPrice", source = "godoAddGoods", qualifiedByName = "calcGodoAddSalesAmount")
    @Mapping(target = "salesUnitPrice", source = "godoAddGoods.goodsPrice")
    @Mapping(target = "goodsDcCost", source = "godoAddGoods.goodsDcPrice")
    @Mapping(target = "memberDcCost", source = "godoAddGoods.memberDcPrice")
    @Mapping(target = "couponGoodsDcPrice", source = "godoAddGoods.couponGoodsDcPrice")
    @Mapping(target = "couponOrderDcPrice", constant = "0")
    @Mapping(target = "couponDeliveryDcPrice", constant = "0")
    @Mapping(target = "useMileage", constant = "0")
    @Mapping(target = "parentOrderDetailSeq", source = "parentSeqNo")
    @Mapping(target = "goodsOption", source = "godoAddGoods.optionInfo")
    @Mapping(target = "foodTypeCode", constant = "GENERAL_TYPE")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    OrderAddGoods toEntity(OrderMaster orderMaster, GodoAddGoods godoAddGoods, int parentSeqNo);

    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "orderDetailSeq", source = "orderMaster.maxOrderDetailSeq")
    @Mapping(target = "soTypeCode", constant = "ZSOA")
    @Mapping(target = "orderGoodsCode", source = "goodsCode")
    @Mapping(target = "orderCnt", source = "orderQty")
    @Mapping(target = "realDeliveryCnt", constant = "0")
    @Mapping(target = "issueDeliveryCnt", constant = "0")
    @Mapping(target = "returnRealYn", constant = "N")
    @Mapping(target = "salesOrderPrice", constant = "0")
    @Mapping(target = "salesUnitPrice", constant = "0")
    @Mapping(target = "goodsDcCost", constant = "0")
    @Mapping(target = "memberDcCost", constant = "0")
    @Mapping(target = "couponGoodsDcPrice", constant = "0")
    @Mapping(target = "couponOrderDcPrice", constant = "0")
    @Mapping(target = "couponDeliveryDcPrice", constant = "0")
    @Mapping(target = "useMileage", constant = "0")
    @Mapping(target = "parentOrderDetailSeq", source = "parentSeqNo")
    @Mapping(target = "foodTypeCode", constant = "GENERAL_TYPE")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    OrderAddGoods toEntity(OrderMaster orderMaster, String goodsCode, int orderQty, int parentSeqNo);

    @Named("calcGodoAddSalesAmount")
    default BigDecimal calcGodoSalesAmount(GodoAddGoods goods) {
        return goods.getGoodsPrice().multiply(BigDecimal.valueOf(goods.getGoodsCnt()));
    }

    @Named("getGodoStatus")
    default String getGodoStatus(String godoOrderStatus) {

        if(!StringUtils.hasText(godoOrderStatus)) {
            return "OS9999";
        }

        return GodoOrderStatus.valueOf(godoOrderStatus.toUpperCase()).value();
    }
}
