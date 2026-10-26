package com.sfood.eai.external.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Stock {

    @Id
    private Long id;

    private String companyCode;

    private String warehouseCode;

    private String goodsCode;

    private Integer quantity;
}
