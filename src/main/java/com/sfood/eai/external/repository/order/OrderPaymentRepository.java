package com.sfood.eai.external.repository.order;

import com.sfood.eai.external.entity.id.order.OrderPaymentId;
import com.sfood.eai.external.entity.order.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderPaymentRepository extends JpaRepository <OrderPayment, OrderPaymentId> {
    Optional<OrderPayment> findByCompanyCodeAndMarketMallCodeAndOrderMergeNo(String companyCode, String mallCode, String orderNo);
}
