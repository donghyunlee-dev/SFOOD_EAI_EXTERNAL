package com.sfood.eai.external.repository.order;

import com.sfood.eai.external.entity.id.order.OrderSequenceId;
import com.sfood.eai.external.entity.order.OrderSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSequenceRepository extends JpaRepository<OrderSequence, OrderSequenceId> {
}
