package com.sfood.eai.external.repository.order;

import com.sfood.eai.external.entity.id.order.OrderAddGoodsId;
import com.sfood.eai.external.entity.order.OrderAddGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderAddGoodsRepository extends JpaRepository <OrderAddGoods, OrderAddGoodsId> {
    List<OrderAddGoods> findByCompanyCodeAndMarketMallCodeAndOrderMergeNo(String companyCode, String mallCode, String orderNo);
    void deleteAllByCompanyCodeAndMarketMallCodeAndOrderMergeNo(String companyCode, String mallCode, String orderNo);
}
