package com.sfood.eai.external.mapper.order;

import com.sfood.eai.external.dto.order.OrderGoodsPriceCalculator;
import com.sfood.eai.external.entity.godo.GodoOrder;
import com.sfood.eai.external.entity.godo.GodoOrderDelivery;
import com.sfood.eai.external.entity.godo.GodoOrderGoods;
import com.sfood.eai.external.entity.order.OrderMaster;
import com.sfood.eai.external.entity.order.OrderPayment;
import com.sfood.eai.external.type.godo.GodoSettleKind;
import com.sfood.eai.external.util.CustomDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderPaymentMapper {
    OrderPaymentMapper INSTANCE = Mappers.getMapper(OrderPaymentMapper.class);

    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "settleKindCode", source = "godoOrder.settleKind", qualifiedByName = "getSettleKindCode")
    @Mapping(target = "totalOrderCost", source = "godoOrder.totalGoodsPrice")
    @Mapping(target = "totalDeliveryCost", source = "godoOrder", qualifiedByName = "getTotalDeliveryCharge")
    @Mapping(target = "totalDcCost", source = "godoOrder.totalDcCost")
    @Mapping(target = "totalGoodsDcCost", source = "godoOrder.totalGoodsDcPrice")
    @Mapping(target = "totalMemberDcCost", source = "godoOrder.totalMemberDcPrice")
    @Mapping(target = "totalCouponGoodsDcCost", source = "godoOrder.totalCouponGoodsDcPrice")
    @Mapping(target = "totalCouponOrderDcCost", source = "godoOrder.totalCouponOrderDcPrice")
    @Mapping(target = "totalCouponDeliveryDcCost", source = "godoOrder.totalCouponDeliveryDcPrice")
    @Mapping(target = "totalUseMileage", source = "godoOrder.useMileage")
    @Mapping(target = "totalUseDeposit", source = "godoOrder.useDeposit")
    @Mapping(target = "totalEnuriDcPrice", source = "godoOrder.totalEnuriDcPrice")
    @Mapping(target = "totalPayCost", source = "godoOrder", qualifiedByName = "getGodoTotalPrice")
    @Mapping(target = "totalWonCost", source = "godoOrder", qualifiedByName = "getGodoTotalSupplyPrice")
    @Mapping(target = "totalTaxVatCost", source = "godoOrder", qualifiedByName = "getGodoTotalVatPrice")
    @Mapping(target = "payDate", source = "orderGoods.paymentDt",qualifiedByName = "convertDateTime")
    @Mapping(target = "paymentMsg", source = "godoOrder.orderChannelFl")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    OrderPayment toEntity(OrderMaster orderMaster, GodoOrder godoOrder, GodoOrderGoods orderGoods);

    // 정기배송 주문 결제  금액은 품목의 금액으로 설정
    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "settleKindCode", source = "godoOrder.settleKind", qualifiedByName = "getSettleKindCode")
    @Mapping(target = "totalOrderCost", source = "godoGoods", qualifiedByName = "getGodoTotalOrderCostByGoods")
    @Mapping(target = "totalDeliveryCost", source = "godoOrderDelivery", qualifiedByName = "getGodoDeliveryChargeByGoods")
    @Mapping(target = "totalDcCost", source = "godoGoods.totalDcPrice")
    @Mapping(target = "totalGoodsDcCost", source = "godoGoods.goodsDcPrice")
    @Mapping(target = "totalMemberDcCost", source = "godoGoods.memberDcPrice")
    @Mapping(target = "totalMemberDeliveryDcCost", constant = "0")
    @Mapping(target = "totalCouponGoodsDcCost", source = "godoGoods.couponGoodsDcPrice")
    @Mapping(target = "totalCouponOrderDcCost", source = "godoGoods.divisionCouponOrderDcPrice")
    @Mapping(target = "totalCouponDeliveryDcCost", constant = "0")
    @Mapping(target = "totalUseMileage", source = "godoGoods", qualifiedByName = "getTotalUseMileageByGoods")
    @Mapping(target = "totalUseDeposit", source = "godoGoods", qualifiedByName = "getTotalUseDepositByGoods")
    @Mapping(target = "totalEnuriDcPrice", source = "godoGoods.enuri")
    @Mapping(target = "totalPayCost", source = "godoGoods", qualifiedByName = "getTotalPayCostByGoods")
    @Mapping(target = "totalWonCost", source = "godoGoods", qualifiedByName = "getGodoTotalSupplyPriceByGoods")
    @Mapping(target = "totalTaxVatCost", source = "godoGoods", qualifiedByName = "getGodoTotalVatPriceByGoods")
    @Mapping(target = "payDate", source = "godoGoods.paymentDt",qualifiedByName = "convertDateTime")
    @Mapping(target = "paymentMsg", source = "godoOrder.orderChannelFl")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    OrderPayment toEntityByGoods(OrderMaster orderMaster, GodoOrder godoOrder, GodoOrderGoods godoGoods, GodoOrderDelivery godoOrderDelivery);

    @Named("getSettleKindCode")
    default String getSettleKindCode(String godoSettleCode) {
        try {
            if (!StringUtils.hasText(godoSettleCode)) {
                return null;
            }

            return GodoSettleKind.valueOf(godoSettleCode.toUpperCase()).value();
        } catch (Exception e) {
            return null;
        }
    }

    @Named("convertDateTime")
    default LocalDateTime convertDateTime(String payDate) {

        if (!StringUtils.hasText(payDate)) {
            return null;
        }
        if ("0000-00-00 00:00:00".equals(payDate)) {
            return null;
        }

        return CustomDateUtils.parseLocalDateTime(payDate, "yyyy-MM-dd HH:mm:ss");
    }

    @Named("getGodoTotalPrice")
    default BigDecimal getGodoTotalPrice(GodoOrder godoOrder) {

        if ("naverpay".equals(godoOrder.getOrderChannelFl())) {
            // 배송비 할인을 받았으면, 할인금액에 이미 배송비가 포함되었기 때문에 배송비를 더해서 상품금액을 맞춘다.
            if (godoOrder.getTotalMemberDeliveryDcPrice().compareTo(BigDecimal.ZERO) > 0 || godoOrder.getTotalCouponDeliveryDcPrice().compareTo(BigDecimal.ZERO) > 0) {
                return godoOrder.getTotalGoodsPrice().subtract(godoOrder.getTotalDcExceptDeliveryCost());
            } else {
                // 상품금액에서 총 할인 금액을 뺀 금액(배송비 제외)이 결제금액이 된다.
                return godoOrder.getTotalGoodsPrice().subtract(godoOrder.getTotalDcCost());
            }
        }

        // 배송비가 결제 금액보다 크면 배송비 일부가 결제된 상태(마일리지, 예치금, 배송비 할인등)
        // 결제 금액에서 배송비를 제외해야 하기 때문에 0원 처리
        if (godoOrder.getTotalDeliveryCost().compareTo(godoOrder.getSettlePrice()) > 0) {
            return BigDecimal.ZERO;
        } else {
            // 배송비 제외 : 결제 금액(배송비 포함금액) - (배송비 - 회원할인(배송비) - 쿠폰할인(배송비))
            return godoOrder.getSettlePrice().subtract(godoOrder.getTotalDeliveryCost());
        }
    }

    @Named("getGodoTotalSupplyPrice")
    default BigDecimal getGodoTotalSupplyPrice(GodoOrder godoOrder) {
        return getGodoTotalPrice(godoOrder).divide(BigDecimal.valueOf(1.1), 0, RoundingMode.DOWN);
    }

    @Named("getGodoTotalVatPrice")
    default BigDecimal getGodoTotalVatPrice(GodoOrder godoOrder) {
        return getGodoTotalPrice(godoOrder).subtract(getGodoTotalSupplyPrice(godoOrder));
    }

    @Named("getTotalDeliveryCharge")
    default BigDecimal getTotalDeliveryCharge(GodoOrder godoOrder) {
        // 예치금 적립금 사용 시 결제 금액 체크
        // 배송비가 결제금액보다 크면 결제금액이 배송비를 결제한 금액이다.
        // 배송비 4000원 중 2000원 결제 금액이면 남은 배송비 2000원으로 결제한 금액이다. 배송비 2000원으로 표시를 한다.
        // 배송비 4000원 결제금액 0이면 배송비를 할인 포함해서 전부 결제한 상태다. 그래서 결제금액 0이 배송비와 같다.
        if (!"naverpay".equals(godoOrder.getOrderChannelFl()) && godoOrder.getTotalDeliveryCost().compareTo(godoOrder.getSettlePrice()) > 0) {
            return godoOrder.getSettlePrice();
        } else {
            return godoOrder.getTotalDeliveryCost();
        }
    }

    @Named("getGodoTotalOrderCostByGoods")
    default BigDecimal getGodoTotalOrderCostByGoods(GodoOrderGoods godoOrderGoods) {
        return godoOrderGoods.getGoodsPrice().add(godoOrderGoods.getOptionPrice());
    }

    @Named("getTotalUseMileageByGoods")
    default BigDecimal getTotalUseMileageByGoods(GodoOrderGoods goods) {
        return (goods.getDivisionUseMileage().add(goods.getDivisionGoodsDeliveryUseMileage()).add(goods.getDivisionCouponOrderMileage()));
    }

    @Named("getTotalUseDepositByGoods")
    default BigDecimal getTotalUseDepositByGoods(GodoOrderGoods goods) {
        return (goods.getDivisionUseDeposit().add(goods.getDivisionGoodsDeliveryUseDeposit()));
    }

    @Named("getTotalPayCostByGoods")
    default BigDecimal getTotalPayCostByGoods(GodoOrderGoods goods) {
        return (goods.getGoodsPrice().add(goods.getOptionPrice()))
                .multiply(BigDecimal.valueOf(goods.getGoodsCnt()))
                .subtract(goods.getTotalDcPrice());
    }

    @Named("getGodoTotalSupplyPriceByGoods")
    default BigDecimal getGodoTotalSupplyPriceByGoods(GodoOrderGoods goods) {
        return getTotalPayCostByGoods(goods)
                .divide(BigDecimal.valueOf(1.1), 0, RoundingMode.DOWN);
    }

    @Named("getGodoTotalVatPriceByGoods")
    default BigDecimal getGodoTotalVatPriceByGoods(GodoOrderGoods goods) {
        return getTotalPayCostByGoods(goods).subtract(getGodoTotalSupplyPriceByGoods(goods));
    }

    @Named("getGodoDeliveryChargeByGoods")
    default BigDecimal getGodoDeliveryChargeByGoods(GodoOrderDelivery delivery) {
        return delivery.getDeliveryCharge()
                .subtract(delivery.getDivisionDeliveryCharge())
                .subtract(delivery.getDivisionMemberDeliveryDcPrice());
        // 회원할인(배송비), 쿠폰할인(배송비)에 금액을 배송비에서 차감
    }

    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "totalOrderCost", source = "totalOrderAmount")
    @Mapping(target = "totalDeliveryCost", source = "totalDeliveryAmount")
    @Mapping(target = "totalDcCost", constant = "0")
    @Mapping(target = "totalGoodsDcCost", constant = "0")
    @Mapping(target = "totalMemberDcCost", constant = "0")
    @Mapping(target = "totalCouponGoodsDcCost", constant = "0")
    @Mapping(target = "totalCouponOrderDcCost", constant = "0")
    @Mapping(target = "totalCouponDeliveryDcCost", constant = "0")
    @Mapping(target = "totalUseMileage", constant = "0")
    @Mapping(target = "totalPayCost", source = "totalPayAmount")
    @Mapping(target = "totalWonCost", source = "totalPayAmount", qualifiedByName = "getSabangTotalSupplyPrice")
    @Mapping(target = "totalTaxVatCost", source = "totalPayAmount", qualifiedByName = "getSabangTotalVatPrice")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    OrderPayment toEntity(OrderMaster orderMaster, BigDecimal totalOrderAmount, BigDecimal totalDeliveryAmount, BigDecimal totalPayAmount);

    @Named("getSabangTotalSupplyPrice")
    default BigDecimal getSabangTotalSupplyPrice(BigDecimal totalPayAmount) {
        return totalPayAmount.divide(BigDecimal.valueOf(1.1), 0, RoundingMode.DOWN);
    }

    @Named("getSabangTotalVatPrice")
    default BigDecimal getSabangTotalVatPrice(BigDecimal totalPayAmount) {
        return totalPayAmount.subtract(getSabangTotalSupplyPrice(totalPayAmount));
    }

    @Mapping(target = "companyCode", source = "orderMaster.companyCode")
    @Mapping(target = "marketMallCode", source = "orderMaster.marketMallCode")
    @Mapping(target = "settleKindCode", constant = "CH")
    @Mapping(target = "totalDcCost", constant = "0")
    @Mapping(target = "totalGoodsDcCost", constant = "0")
    @Mapping(target = "totalMemberDcCost", constant = "0")
    @Mapping(target = "totalCouponGoodsDcCost", constant = "0")
    @Mapping(target = "totalCouponOrderDcCost", constant = "0")
    @Mapping(target = "totalCouponDeliveryDcCost", constant = "0")
    @Mapping(target = "totalUseMileage", constant = "0")
    @Mapping(target = "totalPayCost", source = "orderGoodsPrice.totalSalesPrice")
    @Mapping(target = "totalWonCost", source = "orderGoodsPrice.totalSupplyPrice")
    @Mapping(target = "totalTaxVatCost", source = "orderGoodsPrice.totalVatPrice")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    OrderPayment toEntity(OrderMaster orderMaster, OrderGoodsPriceCalculator orderGoodsPrice);
}
