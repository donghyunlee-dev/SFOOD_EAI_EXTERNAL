package com.sfood.eai.external.repository.order;

import com.sfood.eai.external.entity.order.OrderGoodsStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderGoodsStatusLogRepository extends JpaRepository<OrderGoodsStatusLog, Long> {
}
