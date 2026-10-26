package com.sfood.eai.external.entity;

import com.sfood.eai.external.entity.id.WarehouseId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "TBL_WAREHOUSE_MST")
@Entity
public class Warehouse extends BaseEntity {

    @EmbeddedId
    private WarehouseId id;

    @Column(name ="WAREHOUSE_NAME")
    private String warehouseName;

    @Column(name ="ERP_WAREHOUSE_CODE")
    private String erpWarehouseCode;

    @Column(name ="PARTNER_CODE")
    private String partnerCode;

    @Column(name ="WAREHOUSE_TYPE")
    private String warehouseType;

    @Column(name ="RETURN_WAREHOUSE_YN")
    private String returnWarehouseYn;

    @Column(name ="DISPOSAL_WAREHOUSE_YN")
    private String disposalWarehouseYn;

    @Column(name ="USE_YN")
    private String useYn;

    @Column(name ="DELETE_YN")
    private String deleteYn;
}
