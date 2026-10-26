package com.sfood.eai.external.entity.goods;

import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleGoods {
    private String goodsCode;
    private String goodsName;
    private String erpGoodsCode;
    private String erpGoodsName;
    private String wmsApiYn;
    private String goodsUnit;
    private String warehouseCode;
    private String bundleYn;
    private String barcode;
    private String goodsTemperature;
    private String subscriptionType;
    private String taxYn;
    private int availableQty;
}
