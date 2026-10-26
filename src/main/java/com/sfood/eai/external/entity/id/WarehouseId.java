package com.sfood.eai.external.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@ToString
@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class WarehouseId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name ="COMPANY_CODE")
    private String companyCode;

    @Column(name ="LOGISTICS_CENTER_CODE")
    private String logisticsCenterCode;

    @Column(name ="WAREHOUSE_CODE")
    private String warehouseCode;
}
