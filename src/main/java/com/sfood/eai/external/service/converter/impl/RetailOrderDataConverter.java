package com.sfood.eai.external.service.converter.impl;

import com.sfood.eai.external.dto.order.OrderGoodsPriceCalculator;
import com.sfood.eai.external.entity.retail.RetailDataEntity;
import com.sfood.eai.external.entity.order.*;
import com.sfood.eai.external.entity.retail.EdiOrderData;
import com.sfood.eai.external.mapper.order.OrderDeliveryMapper;
import com.sfood.eai.external.mapper.order.OrderGoodsMapper;
import com.sfood.eai.external.mapper.order.OrderMasterMapper;
import com.sfood.eai.external.mapper.order.OrderPaymentMapper;
import com.sfood.eai.external.service.OrderGoodsService;
import com.sfood.eai.external.service.converter.BaseOrderDataConverter;
import com.sfood.eai.external.service.converter.OrderDataConverter;
import com.sfood.eai.external.type.ChannelType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RetailOrderDataConverter extends BaseOrderDataConverter implements OrderDataConverter {

    public RetailOrderDataConverter(OrderGoodsService orderGoodsService) {
        super(orderGoodsService);
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.RETAIL;
    }

    @Override
    public OrderDataEntity convertOrderEntity(Object rawData) {

        RetailDataEntity retailData = (RetailDataEntity) rawData;
        List<EdiOrderData> ediOrderData = retailData.getOrders();

        OrderMaster orderMaster = OrderMasterMapper.INSTANCE.toEntity(retailData.getCondition(), ediOrderData.getFirst());

        List<OrderGoods> orderGoodsList = new ArrayList<>();
        List<OrderDelivery> orderDeliveryList = new ArrayList<>();
        OrderGoodsPriceCalculator orderGoodsPrice = new OrderGoodsPriceCalculator();

        for (EdiOrderData order : ediOrderData) {
            orderMaster.increaseDetailMaxSequence();
            OrderGoods orderGoods = OrderGoodsMapper.INSTANCE.toEntity(orderMaster, order);
            orderGoodsList.add(orderGoods);
            orderDeliveryList.add(OrderDeliveryMapper.INSTANCE.toEntity(orderMaster, order));
            orderGoodsPrice.add(orderGoods.getSalesOrderPrice(), orderGoods.getSalesSupplyPrice(), orderGoods.getSalesVatPrice());
        }

        OrderPayment orderPayment = OrderPaymentMapper.INSTANCE.toEntity(orderMaster, orderGoodsPrice);

        return OrderDataEntity
                .builder()
                .orderMaster(orderMaster)
                .orderGoods(orderGoodsList)
                .orderDelivery(orderDeliveryList)
                .orderPayment(orderPayment)
                .build();
    }
}
