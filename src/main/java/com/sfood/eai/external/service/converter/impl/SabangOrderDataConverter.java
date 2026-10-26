package com.sfood.eai.external.service.converter.impl;

import com.sfood.eai.external.entity.goods.MallGoodsMatch;
import com.sfood.eai.external.entity.order.*;
import com.sfood.eai.external.entity.sabang.SabangDataEntity;
import com.sfood.eai.external.entity.sabang.SabangOrder;
import com.sfood.eai.external.mapper.order.OrderDeliveryMapper;
import com.sfood.eai.external.mapper.order.OrderGoodsMapper;
import com.sfood.eai.external.mapper.order.OrderMasterMapper;
import com.sfood.eai.external.mapper.order.OrderPaymentMapper;
import com.sfood.eai.external.service.OrderGoodsService;
import com.sfood.eai.external.service.converter.BaseOrderDataConverter;
import com.sfood.eai.external.service.converter.OrderDataConverter;
import com.sfood.eai.external.type.ChannelType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class SabangOrderDataConverter extends BaseOrderDataConverter implements OrderDataConverter {

    public SabangOrderDataConverter(OrderGoodsService orderGoodsService) {
        super(orderGoodsService);
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.SABANGNET;
    }

    @Override
    public OrderDataEntity convertOrderEntity(Object rawData) {
        SabangDataEntity sabangData = (SabangDataEntity) rawData;
        OrderMaster orderMaster = OrderMasterMapper.INSTANCE.toEntity(sabangData.getCondition(), sabangData.getOrders().getFirst());

        List<OrderGoods> orderGoodsList = new ArrayList<>();
        List<OrderDelivery> orderDeliveryList = new ArrayList<>();
        BigDecimal totalOrderAmount = BigDecimal.ZERO;
        BigDecimal totalDeliveryAmount = BigDecimal.ZERO;
        BigDecimal totalPayAmount = BigDecimal.ZERO;

        for (SabangOrder goods : sabangData.getOrders()) {

            List<MallGoodsMatch> matchedGoods = getMallGoods(goods.getCompanyCode(), goods.getMarketMallCode(), goods.getProductId(), goods.getSkuAlias());

            if (!CollectionUtils.isEmpty(matchedGoods)) {
                goods.setCompanyGoodsCd(matchedGoods.getFirst().getId().getGoodsCode());
            }

            orderMaster.increaseDetailMaxSequence();
            orderGoodsList.add(OrderGoodsMapper.INSTANCE.toEntity(orderMaster, goods));
            orderDeliveryList.add(OrderDeliveryMapper.INSTANCE.toEntity(orderMaster, goods));

            totalOrderAmount = totalOrderAmount.add(goods.getTotalCost());
            totalDeliveryAmount = totalDeliveryAmount.add(goods.getDelvCost());
            totalPayAmount = totalPayAmount.add(goods.getPayCost());
        }

        OrderPayment orderPayment = OrderPaymentMapper.INSTANCE.toEntity(orderMaster, totalOrderAmount, totalDeliveryAmount, totalPayAmount);

        return OrderDataEntity
                .builder()
                .orderMaster(orderMaster)
                .orderGoods(orderGoodsList)
                .orderDelivery(orderDeliveryList)
                .orderPayment(orderPayment)
                .build();
    }
}
