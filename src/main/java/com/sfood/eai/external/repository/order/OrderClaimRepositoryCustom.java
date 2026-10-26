package com.sfood.eai.external.repository.order;

import com.sfood.eai.external.entity.order.OrderClaim;

import java.util.List;

public interface OrderClaimRepositoryCustom {
    List<OrderClaim> findOrderClaimsByKey(String marketOrderNo, String marketOrderSubNo);
}
