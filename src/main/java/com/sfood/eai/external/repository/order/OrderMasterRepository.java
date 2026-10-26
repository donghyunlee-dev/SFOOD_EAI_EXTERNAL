package com.sfood.eai.external.repository.order;

import com.sfood.eai.external.entity.id.order.OrderMasterId;
import com.sfood.eai.external.entity.order.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMasterRepository extends JpaRepository <OrderMaster, OrderMasterId>, OrderMasterRepositoryCustom {
   	@Query(value = "SELECT FN_CREATE_ORDER_NO(:companyCode, :mallCode, :orderDate) FROM DUAL", nativeQuery = true)
	String getNewOrderMergeNo(@Param("companyCode") String companyCode, @Param("mallCode") String mallCode, @Param("orderDate") String orderDate);

	List<OrderMaster> findByCompanyCodeAndMarketMallCodeAndMarketOrderNo(String companyCode, String marketMallCode, String marketOrderNo);

	boolean existsByCompanyCodeAndMarketMallCodeAndMarketOrderNo(String companyCode, String marketMallCode, String marketOrderNo);
}
