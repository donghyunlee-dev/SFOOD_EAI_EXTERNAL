package com.sfood.eai.external.repository.order;

import com.sfood.eai.external.dto.order.OrderCloseStatus;
import com.sfood.eai.external.dto.order.SimpleOrderGoods;
import com.sfood.eai.external.dto.stock.StockMovementClosing;
import com.sfood.eai.external.entity.id.order.OrderGoodsId;

import java.util.List;

public interface OrderGoodsRepositoryCustom {

    SimpleOrderGoods findMallOrderGoods(OrderGoodsId orderGoodsId);

    List<OrderCloseStatus> findClosedOrderGoodsIds();

    List<StockMovementClosing> getStockMovementMaxTransDate();
}
