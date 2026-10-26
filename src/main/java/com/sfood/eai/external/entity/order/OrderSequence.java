package com.sfood.eai.external.entity.order;

import com.sfood.eai.external.entity.id.order.OrderSequenceId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "TBL_ORDER_SEQUENCE")
@Entity
public class OrderSequence  {
    @EmbeddedId
    private OrderSequenceId id;

    @Column(name = "ORDER_SEQ")
    private int orderSeq;
}
