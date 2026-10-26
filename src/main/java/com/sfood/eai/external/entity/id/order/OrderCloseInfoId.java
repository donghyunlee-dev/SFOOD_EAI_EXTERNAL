package com.sfood.eai.external.entity.id.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderCloseInfoId {

    private static final long serialVersionUID = 1L;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "ORDER_MERGE_NO")
    private String orderMergeNo;

    @Column(name = "ORDER_DETAIL_SEQ")
    private Integer orderDetailSeq;
}
