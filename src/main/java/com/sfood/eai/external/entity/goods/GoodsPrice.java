package com.sfood.eai.external.entity.goods;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class GoodsPrice {
    private String companyCode;
    private String goodsCode;
    private String bundleYn;
    private String erpGoodsCode;
    private String goodsName;
    private String exclusiveYn;
    private String salesStatus;
    private BigDecimal supplyValue;
    private BigDecimal vatValue;
    private String taxYn;
    private String category1;
    private String category2;
    private String category3;
    private String category4;
    private String optionYn;
    private String optionType;
    private String optionTitle;
    private String wmsApiYn;
    private String warehouseCode;
    private String originCode;
    private String supplierCode;
    private String goodsUnit;
    private String barcode;
    private String goodsTemperature;
    private String subscriptionType;
    private String expirationMonth;
    private String expirationDay;
    private String useYn;
    private String goodsDescription;
    private String adminMemo;
    private String goodsNameShort;
    private String goodsKeywords;
}
