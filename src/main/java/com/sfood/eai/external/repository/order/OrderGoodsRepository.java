package com.sfood.eai.external.repository.order;

import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import com.sfood.eai.external.entity.order.OrderGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderGoodsRepository extends JpaRepository <OrderGoods, OrderGoodsId>, OrderGoodsRepositoryCustom {

	List<OrderGoods> findByCompanyCodeAndMarketMallCodeAndOrderMergeNo(String companyCode, String mallCode, String orderNo);

	OrderGoods findByCompanyCodeAndMarketMallCodeAndMarketOrderNoAndMarketOrderSubNo(String companyCode, String marketMallCode, String marketOrderNo, String marketOrderSubNo);

	boolean existsByCompanyCodeAndMarketMallCodeAndMarketOrderNoAndMarketOrderSubNo(String companyCode, String marketMallCode, String marketOrderNo, String marketOrderSubNo);

	void deleteByCompanyCodeAndMarketMallCodeAndOrderMergeNo(String companyCode, String marketMallCode, String orderMergeNo);

}
