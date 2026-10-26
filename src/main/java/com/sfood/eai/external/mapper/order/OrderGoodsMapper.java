package com.sfood.eai.external.mapper.order;


import com.sfood.eai.external.dto.order.DeliverySchedule;
import com.sfood.eai.external.entity.godo.GodoAddGoods;
import com.sfood.eai.external.entity.godo.GodoGift;
import com.sfood.eai.external.entity.godo.GodoOrderGoods;
import com.sfood.eai.external.entity.goods.Goods;
import com.sfood.eai.external.entity.goods.GoodsPrice;
import com.sfood.eai.external.entity.order.OrderGoods;
import com.sfood.eai.external.entity.order.OrderMaster;
import com.sfood.eai.external.entity.retail.EdiOrderData;
import com.sfood.eai.external.entity.sabang.SabangOrder;
import com.sfood.eai.external.type.OrderStatus;
import com.sfood.eai.external.type.godo.GodoOrderStatus;
import com.sfood.eai.external.type.sabang.SabangOrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderGoodsMapper {

    OrderGoodsMapper INSTANCE = Mappers.getMapper(OrderGoodsMapper.class);
    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "orderDetailSeq", source = "orderMaster.maxOrderDetailSeq")
    @Mapping(target = "marketOrderSubNo", source = "godoOrderGoods.sno")
    @Mapping(target = "orderStatusCode", source = "godoOrderGoods.orderStatus", qualifiedByName = "getGodoStatus")
    @Mapping(target = "soTypeCode", constant = "ZSOA")
    @Mapping(target = "orderGoodsCode", source = "godoOrderGoods.goodsCd")
    @Mapping(target = "marketGoodsCode", source = "godoOrderGoods.goodsNo")
    @Mapping(target = "marketGoodsName", source = "godoOrderGoods.goodsNm")
    @Mapping(target = "orderCnt", source = "godoOrderGoods.goodsCnt")
    @Mapping(target = "realDeliveryCnt", constant = "0")
    @Mapping(target = "issueDeliveryCnt", constant = "0")
    @Mapping(target = "returnRealYn", constant = "N")
    @Mapping(target = "salesOrderPrice", source = "godoOrderGoods", qualifiedByName = "calcGodoOrderSalesAmount")
    @Mapping(target = "salesUnitPrice", source = "godoOrderGoods", qualifiedByName = "calcGodoOrderSalesPrice")
    @Mapping(target = "originSalesUnitPrice", source = "godoOrderGoods.goodsPrice")
    @Mapping(target = "dcCost", source = "godoOrderGoods.totalDcPrice")
    @Mapping(target = "goodsDcCost", source = "godoOrderGoods.goodsDcPrice")
    @Mapping(target = "memberDcCost", source = "godoOrderGoods.memberDcPrice")
    @Mapping(target = "couponGoodsDcCost", source = "godoOrderGoods.couponGoodsDcPrice")
    @Mapping(target = "couponOrderDcCost", source = "godoOrderGoods.divisionCouponOrderDcPrice")
    @Mapping(target = "couponDeliveryDcCost", constant = "0")
    @Mapping(target = "useMileage", source = "godoOrderGoods", qualifiedByName = "calcUseMileage")
    @Mapping(target = "useDeposit", source = "godoOrderGoods", qualifiedByName = "calcUseDeposit")
    @Mapping(target = "enuriDcPrice", source = "godoOrderGoods.enuri")
    @Mapping(target = "parentOrderDetailSeq", constant = "0")
    @Mapping(target = "maxOrderClaimSeq", source = "godoOrderGoods.orderGroupCd")
    @Mapping(target = "foodTypeCode", constant = "GENERAL_TYPE")
    @Mapping(target = "receiptYn", source= "orderMaster", qualifiedByName = "convertReceiptFlag")
    @Mapping(target = "giftYn", constant = "N")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    @Mapping(target = "new", constant = "true")
    OrderGoods toEntity(OrderMaster orderMaster, GodoOrderGoods godoOrderGoods);

    @Named("convertReceiptFlag")
    default String convertReceiptFlag(OrderMaster orderMaster) {
        return ObjectUtils.isEmpty(orderMaster.getReceiptFl()) ? "N" : "Y";
    }

    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "orderDetailSeq", source = "orderMaster.maxOrderDetailSeq")
    @Mapping(target = "marketOrderSubNo", source = "godoOrderGoods.sno")
    @Mapping(target = "orderStatusCode", source = "godoOrderGoods.orderStatus", qualifiedByName = "getGodoStatus")
    @Mapping(target = "soTypeCode", constant = "ZSOA")
    @Mapping(target = "orderGoodsCode", source = "subGoods.goodsCode")
    @Mapping(target = "marketGoodsCode", source = "godoOrderGoods.goodsNo")
    @Mapping(target = "marketGoodsName", source = "godoOrderGoods.goodsNm")
    @Mapping(target = "orderCnt", source = "godoOrderGoods.goodsCnt")
    @Mapping(target = "realDeliveryCnt", constant = "0")
    @Mapping(target = "issueDeliveryCnt", constant = "0")
    @Mapping(target = "returnRealYn", constant = "N")
    @Mapping(target = "salesOrderPrice", source = "godoOrderGoods", qualifiedByName = "calcGodoOrderSalesAmount")
    @Mapping(target = "salesUnitPrice", source = "godoOrderGoods", qualifiedByName = "calcGodoOrderSalesPrice")
    @Mapping(target = "originSalesUnitPrice", source = "godoOrderGoods.goodsPrice")
    @Mapping(target = "dcCost", source = "godoOrderGoods.totalDcPrice")
    @Mapping(target = "goodsDcCost", source = "godoOrderGoods.goodsDcPrice")
    @Mapping(target = "memberDcCost", source = "godoOrderGoods.memberDcPrice")
    @Mapping(target = "couponGoodsDcCost", source = "godoOrderGoods.couponGoodsDcPrice")
    @Mapping(target = "couponOrderDcCost", source = "godoOrderGoods.divisionCouponOrderDcPrice")
    @Mapping(target = "couponDeliveryDcCost", constant = "0")
    @Mapping(target = "useMileage", source = "godoOrderGoods", qualifiedByName = "calcUseMileage")
    @Mapping(target = "useDeposit", source = "godoOrderGoods", qualifiedByName = "calcUseDeposit")
    @Mapping(target = "enuriDcPrice", source = "godoOrderGoods.enuri")
    @Mapping(target = "parentOrderDetailSeq", constant = "0")
    @Mapping(target = "goodsOption", source = "subGoods.optionTitle")
    @Mapping(target = "foodTypeCode", constant = "GENERAL_TYPE")
    @Mapping(target = "giftYn", constant = "N")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    @Mapping(target = "new", constant = "true")
    OrderGoods toEntity(OrderMaster orderMaster, GodoOrderGoods godoOrderGoods, Goods subGoods);

    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "orderDetailSeq", source = "orderMaster.maxOrderDetailSeq")
    @Mapping(target = "marketOrderSubNo", source = "godoOrderGoods.sno")
    @Mapping(target = "orderStatusCode", source = "godoOrderGoods.orderStatus", qualifiedByName = "getGodoStatus")
    @Mapping(target = "soTypeCode", constant = "ZSOA")
    @Mapping(target = "orderGoodsCode", source = "goodsPrice.goodsCode")
    @Mapping(target = "marketGoodsCode", source = "godoOrderGoods.goodsNo")
    @Mapping(target = "marketGoodsName", source = "godoOrderGoods.goodsNm")
    @Mapping(target = "orderCnt", constant = "1")
    @Mapping(target = "realDeliveryCnt", constant = "0")
    @Mapping(target = "issueDeliveryCnt", constant = "0")
    @Mapping(target = "returnRealYn", constant = "N")
    @Mapping(target = "salesOrderPrice", source = "goodsPrice", qualifiedByName = "calcGoodsPrice")
    @Mapping(target = "salesUnitPrice", source = "goodsPrice", qualifiedByName = "calcGoodsPrice")
    @Mapping(target = "originSalesUnitPrice", source = "goodsPrice", qualifiedByName = "calcGoodsPrice")
    @Mapping(target = "dcCost", constant = "0")
    @Mapping(target = "goodsDcCost", constant = "0")
    @Mapping(target = "memberDcCost", constant = "0")
    @Mapping(target = "couponGoodsDcCost", constant = "0")
    @Mapping(target = "couponOrderDcCost", constant = "0")
    @Mapping(target = "couponDeliveryDcCost", constant = "0")
    @Mapping(target = "useMileage", source = "godoOrderGoods", qualifiedByName = "calcUseMileage")
    @Mapping(target = "useDeposit", source = "godoOrderGoods", qualifiedByName = "calcUseDeposit")
    @Mapping(target = "enuriDcPrice", source = "godoOrderGoods.enuri")
    @Mapping(target = "parentOrderDetailSeq", constant = "0")
    @Mapping(target = "goodsOption", source = "deliverySchedule.scheduleName")
    @Mapping(target = "scheduleCode", source = "deliverySchedule.scheduleCode")
    @Mapping(target = "foodTypeCode", constant = "GENERAL_TYPE")
    @Mapping(target = "giftYn", constant = "N")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    @Mapping(target="subscriptionType", ignore = true)
    @Mapping(target = "new", constant = "true")
    OrderGoods toEntity(OrderMaster orderMaster, GodoOrderGoods godoOrderGoods, GoodsPrice goodsPrice, DeliverySchedule deliverySchedule);

    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "orderMergeNo", source = "orderMaster.orderMergeNo")
    @Mapping(target = "orderDetailSeq", source = "orderMaster.maxOrderDetailSeq")
    @Mapping(target = "marketOrderSubNo", source = "orderGoods.marketOrderSubNo")
    @Mapping(target = "orderStatusCode", source = "orderGoods.orderStatusCode")
    @Mapping(target = "soTypeCode", constant = "ZSOA")
    @Mapping(target = "orderGoodsCode", source = "goodsPrice.goodsCode")
    @Mapping(target = "marketOrderNo", source = "orderGoods.marketOrderNo")
    @Mapping(target = "marketGoodsCode", source = "orderGoods.marketGoodsCode")
    @Mapping(target = "marketGoodsName", source = "orderGoods.marketGoodsName")
    @Mapping(target = "marketOptionCode", source = "orderGoods.marketOptionCode")
    @Mapping(target = "erpGoodsCode", source = "orderGoods.erpGoodsCode")
    @Mapping(target = "orderCnt", constant = "1")
    @Mapping(target = "realDeliveryCnt", constant = "0")
    @Mapping(target = "issueDeliveryCnt", constant = "0")
    @Mapping(target = "returnRealYn", constant = "N")
    @Mapping(target = "salesOrderPrice", source = "goodsPrice", qualifiedByName = "calcGoodsPrice")
    @Mapping(target = "salesUnitPrice", source = "goodsPrice", qualifiedByName = "calcGoodsPrice")
    @Mapping(target = "originSalesUnitPrice", source = "goodsPrice", qualifiedByName = "calcGoodsPrice")
    @Mapping(target = "dcCost", constant = "0")
    @Mapping(target = "goodsDcCost", constant = "0")
    @Mapping(target = "memberDcCost", constant = "0")
    @Mapping(target = "couponGoodsDcCost", constant = "0")
    @Mapping(target = "couponOrderDcCost", constant = "0")
    @Mapping(target = "couponDeliveryDcCost", constant = "0")
    @Mapping(target = "useMileage", source = "orderGoods.useMileage")
    @Mapping(target = "useDeposit", source = "orderGoods.useDeposit")
    @Mapping(target = "enuriDcPrice", source = "orderGoods.enuriDcPrice")
    @Mapping(target = "parentOrderDetailSeq", constant = "0")
    @Mapping(target = "goodsOption", source = "deliverySchedule.scheduleName")
    @Mapping(target = "scheduleCode", source = "deliverySchedule.scheduleCode")
    @Mapping(target = "foodTypeCode", constant = "GENERAL_TYPE")
    @Mapping(target = "giftYn", constant = "N")
    @Mapping(target = "bundleYn", source = "orderGoods.bundleYn")
    @Mapping(target = "barcode", source = "orderGoods.barcode")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    @Mapping(target="subscriptionType", ignore = true)
    @Mapping(target = "new", constant = "true")
    OrderGoods toEntity(OrderMaster orderMaster, OrderGoods orderGoods, GoodsPrice goodsPrice, DeliverySchedule deliverySchedule);

    @Named("calcGoodsPrice")
    default BigDecimal calcGoodsPrice(GoodsPrice goodsPrice) {
        return (goodsPrice.getSupplyValue().add(goodsPrice.getVatValue()));
    }

    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "orderDetailSeq", source = "orderMaster.maxOrderDetailSeq")
    @Mapping(target = "marketOrderSubNo", source = "godoOrderGoods.sno")
    @Mapping(target = "orderStatusCode", source = "godoAddGoods.orderStatus", qualifiedByName = "getGodoStatus")
    @Mapping(target = "soTypeCode", constant = "ZSOA")
    @Mapping(target = "orderGoodsCode", source = "godoAddGoods.goodsCd")
    @Mapping(target = "marketGoodsCode", source = "godoAddGoods.addGoodsNo")
    @Mapping(target = "marketGoodsName", source = "godoAddGoods.goodsNm")
    @Mapping(target = "orderCnt", source = "goodsQty")
    @Mapping(target = "realDeliveryCnt", constant = "0")
    @Mapping(target = "issueDeliveryCnt", constant = "0")
    @Mapping(target = "returnRealYn", constant = "N")
    @Mapping(target = "salesOrderPrice", source = "goodsPrice", qualifiedByName = "calcAddGoodsPrice")
    @Mapping(target = "salesUnitPrice", source = "goodsPrice", qualifiedByName = "calcAddGoodsPrice")
    @Mapping(target = "originSalesUnitPrice", source = "goodsPrice", qualifiedByName = "calcAddGoodsPrice")
    @Mapping(target = "goodsDcCost", source = "godoAddGoods.goodsDcPrice")
    @Mapping(target = "memberDcCost", source = "godoAddGoods.memberDcPrice")
    @Mapping(target = "couponGoodsDcCost", source = "godoAddGoods.couponGoodsDcPrice")
    @Mapping(target = "couponOrderDcCost", constant = "0")
    @Mapping(target = "couponDeliveryDcCost", constant = "0")
    @Mapping(target = "useMileage", source = "godoAddGoods", qualifiedByName = "calcAddGoodsUseMileage")
    @Mapping(target = "useDeposit", source = "godoAddGoods", qualifiedByName = "calcAddGoodsUseDeposit")
    @Mapping(target = "goodsOption", source = "deliverySchedule.scheduleName")
    @Mapping(target = "scheduleCode", source = "deliverySchedule.scheduleCode")
    @Mapping(target = "foodTypeCode", constant = "GENERAL_TYPE")
    @Mapping(target = "giftYn", constant = "N")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    @Mapping(target="subscriptionType", ignore = true)
    @Mapping(target = "new", constant = "true")
    OrderGoods toEntity(OrderMaster orderMaster, GodoOrderGoods godoOrderGoods, GodoAddGoods godoAddGoods, GoodsPrice goodsPrice, int goodsQty, DeliverySchedule deliverySchedule);

    @Named("calcGodoOrderSalesAmount")
    default BigDecimal calcGodoSalesAmount(GodoOrderGoods goods) {
        BigDecimal salesOrderPrice =(goods.getGoodsPrice().add(goods.getOptionPrice())).multiply(BigDecimal.valueOf(goods.getGoodsCnt())).subtract(goods.getTotalDcPrice());

        if (salesOrderPrice.compareTo(BigDecimal.ZERO) < 0) {
            salesOrderPrice = salesOrderPrice.add(goods.getDivisionGoodsDeliveryUseMileage()).add(goods.getDivisionGoodsDeliveryUseDeposit());
        }

        return salesOrderPrice;
    }

    @Named("calcGodoOrderSalesPrice")
    default BigDecimal calcGodoOrderSalesPrice(GodoOrderGoods goods) {
        BigDecimal salesAmount = calcGodoSalesAmount(goods);
        return salesAmount.divide(BigDecimal.valueOf(goods.getGoodsCnt()), 4, RoundingMode.DOWN);
    }

    @Named("calcAddGoodsPrice")
    default BigDecimal calcAddGoodsPrice(GoodsPrice goodsPrice) {
        return (goodsPrice.getSupplyValue().add(goodsPrice.getVatValue()));
    }

    @Named("calcUseMileage")
    default BigDecimal calcUseMileage(GodoOrderGoods goods) {
        return (goods.getDivisionUseMileage().add(goods.getDivisionGoodsDeliveryUseMileage()).add(goods.getDivisionCouponOrderMileage()));
    }

    @Named("calcUseDeposit")
    default BigDecimal calcUseDeposit(GodoOrderGoods goods) {
        return (goods.getDivisionUseDeposit().add(goods.getDivisionGoodsDeliveryUseDeposit()));
    }

    @Named("calcAddGoodsUseMileage")
    default BigDecimal calcAddGoodsUseMileage(GodoAddGoods goods) {
        return (goods.getDivisionUseMileage().add(goods.getDivisionGoodsDeliveryUseMileage()).add(goods.getDivisionCouponOrderMileage()));
    }

    @Named("calcAddGoodsUseDeposit")
    default BigDecimal calcAddGoodsUseDeposit(GodoAddGoods goods) {
        return (goods.getDivisionUseDeposit().add(goods.getDivisionGoodsDeliveryUseDeposit()));
    }

    @Named("getGodoStatus")
    default String getGodoStatus(String godoOrderStatus) {

        if(!StringUtils.hasText(godoOrderStatus)) {
            return "OS9999";
        }

        return GodoOrderStatus.valueOf(godoOrderStatus.toUpperCase()).value();
    }

    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "orderDetailSeq", source = "orderMaster.maxOrderDetailSeq")
    @Mapping(target = "marketOrderSubNo", source = "gift.sno")
    @Mapping(target = "orderStatusCode", source = "orderMaster.orderStatus", qualifiedByName = "getGodoStatus")
    @Mapping(target = "soTypeCode", constant = "DTO2")
    @Mapping(target = "orderGoodsCode", source = "gift.giftCd")
    @Mapping(target = "marketGoodsCode", source = "gift.giftNo")
    @Mapping(target = "marketGoodsName", source = "gift.giftNm")
    @Mapping(target = "orderCnt", source = "gift.giftCnt")
    @Mapping(target = "realDeliveryCnt", constant = "0")
    @Mapping(target = "issueDeliveryCnt", constant = "0")
    @Mapping(target = "returnRealYn", constant = "N")
    @Mapping(target = "salesOrderPrice", constant = "0")
    @Mapping(target = "salesUnitPrice", constant = "0")
    @Mapping(target = "dcCost", constant = "0" )
    @Mapping(target = "goodsDcCost", constant = "0")
    @Mapping(target = "memberDcCost", constant = "0")
    @Mapping(target = "couponGoodsDcCost", constant = "0")
    @Mapping(target = "couponOrderDcCost", constant = "0")
    @Mapping(target = "couponDeliveryDcCost", constant = "0")
    @Mapping(target = "useMileage", constant = "0")
    @Mapping(target = "useDeposit", constant = "0")
    @Mapping(target = "enuriDcPrice", constant = "0")
    @Mapping(target = "parentOrderDetailSeq", constant = "0")
    @Mapping(target = "foodTypeCode", constant = "GENERAL_TYPE")
    @Mapping(target = "giftYn", constant = "Y")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    @Mapping(target="subscriptionType", ignore = true)
    @Mapping(target = "new", constant = "true")
    OrderGoods toEntity(OrderMaster orderMaster, GodoGift gift);

    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "orderDetailSeq", source = "orderMaster.maxOrderDetailSeq")
    @Mapping(target = "marketOrderSubNo", source = "sabangOrderGoods.idx")
    @Mapping(target = "orderStatusCode", source = "sabangOrderGoods.orderStatus", qualifiedByName = "getSabangStatus")
    @Mapping(target = "soTypeCode", constant = "ZSOA")
    @Mapping(target = "orderGoodsCode", source = "sabangOrderGoods.companyGoodsCd")
    @Mapping(target = "marketGoodsCode", source = "sabangOrderGoods.productId")
    @Mapping(target = "marketGoodsName", source = "sabangOrderGoods.productName")
    @Mapping(target = "marketOptionCode", source = "sabangOrderGoods.skuAlias")
    @Mapping(target = "orderCnt", source = "sabangOrderGoods.saleCnt")
    @Mapping(target = "realDeliveryCnt", constant = "0")
    @Mapping(target = "issueDeliveryCnt", constant = "0")
    @Mapping(target = "returnRealYn", constant = "N")
    @Mapping(target = "salesOrderPrice", source = "sabangOrderGoods.payCost")
    @Mapping(target = "salesUnitPrice", source = "sabangOrderGoods", qualifiedByName = "calcSabangUnitPrice")
    @Mapping(target = "originSalesUnitPrice", source = "sabangOrderGoods", qualifiedByName = "calcSabangUnitPrice")
    @Mapping(target = "dcCost", constant = "0")
    @Mapping(target = "goodsDcCost", constant = "0")
    @Mapping(target = "memberDcCost", constant = "0")
    @Mapping(target = "couponGoodsDcCost", constant = "0")
    @Mapping(target = "couponOrderDcCost", constant = "0")
    @Mapping(target = "couponDeliveryDcCost", constant = "0")
    @Mapping(target = "useMileage", constant = "0")
    @Mapping(target = "parentOrderDetailSeq", constant = "0")
    @Mapping(target = "goodsOption", source = "sabangOrderGoods.skuValue")
    @Mapping(target = "foodTypeCode", constant = "GENERAL_TYPE")
    @Mapping(target = "giftYn", constant = "N")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    @Mapping(target="subscriptionType", ignore = true)
    @Mapping(target = "new", constant = "true")
    OrderGoods toEntity(OrderMaster orderMaster, SabangOrder sabangOrderGoods);

    @Named("getSabangStatus")
    default String getSabangStatus(String orderStatus) {

        if(!StringUtils.hasText(orderStatus)) {
            return "999";
        }

        SabangOrderStatus sabangOrderStatus = SabangOrderStatus.fromName(orderStatus);

        return ObjectUtils.isEmpty(sabangOrderStatus) ? OrderStatus.OS8040.code() : sabangOrderStatus.value();
    }

    @Named("calcSabangUnitPrice")
    default BigDecimal calcSabangUnitPrice(SabangOrder goods) {
        return goods.getPayCost().divide(BigDecimal.valueOf(goods.getSaleCnt()),4, RoundingMode.DOWN);
    }

    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "orderDetailSeq", source = "orderMaster.maxOrderDetailSeq")
    @Mapping(target = "marketOrderSubNo", source = "ediOrderGoods.lineNo")
    @Mapping(target = "orderStatusCode", constant = "OS1020")
    @Mapping(target = "soNo", source = "ediOrderGoods.transRate")
    @Mapping(target = "soTypeCode", constant = "ZSOA")
    @Mapping(target = "orderGoodsCode", source = "ediOrderGoods.itemCd")
    @Mapping(target = "orderGoodsName", source = "ediOrderGoods.itemNm")
    @Mapping(target = "marketGoodsCode", source = "ediOrderGoods.custItemCd")
    @Mapping(target = "marketGoodsName", source = "ediOrderGoods.custItemNm")
    @Mapping(target = "orderCnt", source = "ediOrderGoods.soQty")
    @Mapping(target = "realDeliveryCnt", constant = "0")
    @Mapping(target = "issueDeliveryCnt", constant = "0")
    @Mapping(target = "returnRealYn", constant = "N")
    @Mapping(target = "salesOrderPrice", source = "ediOrderGoods", qualifiedByName = "calcEdiAmount")
    @Mapping(target = "salesSupplyPrice", source = "ediOrderGoods", qualifiedByName = "calcEdiPrice")
    @Mapping(target = "salesVatPrice", source = "ediOrderGoods", qualifiedByName = "calcEdiVat")
    @Mapping(target = "salesUnitPrice", source = "ediOrderGoods", qualifiedByName = "calcEdiUnitAmount")
    @Mapping(target = "originSalesUnitPrice", source = "ediOrderGoods", qualifiedByName = "calcEdiUnitAmount")
    @Mapping(target = "salesSupplyUnitPrice", source = "ediOrderGoods.soPrice")
    @Mapping(target = "salesVatUnitPrice", source = "ediOrderGoods", qualifiedByName = "calcEdiUnitVat")
    @Mapping(target = "dcCost", constant = "0")
    @Mapping(target = "goodsDcCost", constant = "0")
    @Mapping(target = "memberDcCost", constant = "0")
    @Mapping(target = "couponGoodsDcCost", constant = "0")
    @Mapping(target = "couponOrderDcCost", constant = "0")
    @Mapping(target = "couponDeliveryDcCost", constant = "0")
    @Mapping(target = "useMileage", constant = "0")
    @Mapping(target = "parentOrderDetailSeq", constant = "0")
    @Mapping(target = "foodTypeCode", constant = "GENERAL_TYPE")
    @Mapping(target = "giftYn", constant = "N")
    @Mapping(target = "ECenterCode", source = "ediOrderGoods.ECenterCd")
    @Mapping(target = "ECenterName", source = "ediOrderGoods.ECenterNm")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    @Mapping(target="subscriptionType", ignore = true)
    @Mapping(target = "new", constant = "true")
    OrderGoods toEntity(OrderMaster orderMaster, EdiOrderData ediOrderGoods);

    @Named("calcEdiUnitVat")
    default BigDecimal calcEdiUnitVat(EdiOrderData order) {
        return order.getSoPrice().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.UP);
    }

    @Named("calcEdiUnitAmount")
    default BigDecimal calcEdiUnitAmount(EdiOrderData order) {
        return order.getSoPrice().add(order.getSoPrice().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.UP));
    }

    @Named("calcEdiPrice")
    default BigDecimal calcEdiPrice(EdiOrderData order) {
        if(ObjectUtils.isEmpty(order.getSoQty())) {
            return BigDecimal.ZERO;
        }
        return order.getSoPrice().multiply(BigDecimal.valueOf(order.getSoQty()));
    }

    @Named("calcEdiVat")
    default BigDecimal calcEdiVat(EdiOrderData order) {
        if(ObjectUtils.isEmpty(order.getSoQty())) {
            return BigDecimal.ZERO;
        }
        return order.getSoPrice().multiply(BigDecimal.valueOf(order.getSoQty())).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.UP);
    }

    @Named("calcEdiAmount")
    default BigDecimal calcEdiAmount(EdiOrderData order) {
        if(ObjectUtils.isEmpty(order.getSoQty())) {
            return BigDecimal.ZERO;
        }

        return order.getSoPrice().multiply(BigDecimal.valueOf(order.getSoQty()))
                .add(order.getSoPrice().multiply(BigDecimal.valueOf(order.getSoQty())).multiply(BigDecimal.valueOf(0.1))).setScale(0, RoundingMode.UP);
    }
}
