package com.sfood.eai.external.service;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.order.*;
import com.sfood.eai.external.entity.goods.Goods;
import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import com.sfood.eai.external.entity.partner.Partner;
import com.sfood.eai.external.type.ChannelType;
import com.sfood.eai.external.type.OrderStatus;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService<T> {

    CommonDataRes initOrder(OrderRequest orderRequest);

    boolean existsOrder(String companyCode, String marketMallCode, String marketOrderNo);

    boolean existsOrderGoods(String companyCode, String marketMallCode, String marketOrderNo, String marketOrderSubNo);

    boolean existsOrderClaims(String marketOrderNo, String marketOrderSubNo);

    void saveOrderOne(ChannelType channelType, T rawOrderData);

    void updateOrderOne(ChannelType channel, T rawOrderData);

    void saveOrderAll(ChannelType channelType, List<T> rawOrderDataList);

    void updateOrderAll(ChannelType channel, List<T> rawOrderDataList);

    void saveSubscriptionOrder(ChannelType channelType, T rawOrderData);

    void updateSubscriptionOrder(ChannelType channelType, T rawOrderData);

    void saveClaimOrder(ChannelType channelType, OrderStatus status, T claim);

    void saveSubscriptionClaimOrder(ChannelType channel, OrderStatus status, Object claimOrder);

    CompletableFuture<CommonDataRes> resolveErrorOrder(ErrorOrderUpdateReq errorOrderSearch);

    SimpleOrderGoods getSimpleOrderGoods(OrderGoodsId orderGoodsId);

    void updatePartner(MallOrderId orderId, Partner partner);

    void updateGoods(MallOrderGoodsId orderGoodsId, Goods goods, double orderQty);
}
