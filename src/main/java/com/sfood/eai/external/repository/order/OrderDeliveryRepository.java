package com.sfood.eai.external.repository.order;

import com.sfood.eai.external.entity.order.OrderDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfood.eai.external.entity.id.order.OrderGoodsId;

import java.util.List;

@Repository
public interface OrderDeliveryRepository extends JpaRepository <OrderDelivery, OrderGoodsId> {
    List<OrderDelivery> findByCompanyCodeAndMarketMallCodeAndOrderMergeNo(String companyCode, String mallCode, String orderNo);

    void deleteByCompanyCodeAndMarketMallCodeAndOrderMergeNo(String companyCode, String marketMallCode, String orderMergeNo);
}
