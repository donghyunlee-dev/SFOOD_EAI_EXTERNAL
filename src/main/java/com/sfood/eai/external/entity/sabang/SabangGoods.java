package com.sfood.eai.external.entity.sabang;

import com.sfood.eai.external.entity.id.sabang.SabangGoodsId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@IdClass(SabangGoodsId.class)
@Table(name = "TBL_GOODS_SABANG")
@Entity
public class SabangGoods extends BaseEntity {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Id
    @Column(name = "GOODS_CODE")
    private String goodsCode;
    @Column(name = "MALL_GOODS_CODE")

    private String mallGoodsCode;
    @Column(name = "MALL_GOODS_NAME")
    private String mallGoodsName;

    @Column(name = "MALL_SUPPLIER")
    private String mallSupplier;

    @Column(name = "MALL_ORIGIN_CUSTOM")
    private String mallOriginCustom;

    @Column(name = "MALL_SEARCH_KEYWORD")
    private String mallSearchKeyword;

    @Column(name = "MALL_GOODS_STATUS")
    private String mallGoodsStatus;

    @Column(name = "MALL_COST_VALUE")
    private String mallCostValue;

    @Column(name = "EXT1")
    private String ext1;

    @Column(name = "EXT2")
    private String ext2;
}
