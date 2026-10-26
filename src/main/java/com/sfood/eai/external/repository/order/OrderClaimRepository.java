package com.sfood.eai.external.repository.order;

import com.sfood.eai.external.entity.id.order.OrderClaimId;
import com.sfood.eai.external.entity.order.OrderClaim;
import com.sfood.eai.external.entity.order.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderClaimRepository extends JpaRepository<OrderClaim, OrderClaimId>, OrderClaimRepositoryCustom {

    @Query(value = "SELECT a FROM OrderClaim a WHERE a.id.companyCode=:companyCode and a.id.marketMallCode=:marketMallCode and a.id.orderMergeNo=:orderMergeNo")
    List<OrderClaim> findByCompanyCodeAndMarketMallCodeAndOrderMergeNo(@Param("companyCode") String companyCode, @Param("marketMallCode") String marketMallCode, @Param("orderMergeNo") String orderMergeNo);

    @Modifying
    @Query(value = "DELETE FROM OrderClaim a WHERE a.id.companyCode=:companyCode and a.id.marketMallCode=:marketMallCode and a.id.orderMergeNo=:orderMergeNo")
    void deleteByCompanyCodeAndMarketMallCodeAndOrderMergeNo(@Param("companyCode") String companyCode, @Param("marketMallCode") String marketMallCode, @Param("orderMergeNo") String orderMergeNo);
}
