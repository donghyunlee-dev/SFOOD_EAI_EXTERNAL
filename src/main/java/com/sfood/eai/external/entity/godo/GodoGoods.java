package com.sfood.eai.external.entity.godo;

import com.sfood.eai.external.entity.id.godo.GodoGoodsId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@IdClass(GodoGoodsId.class)
@Table(name = "TBL_GOODS_GODO")
@Entity
public class GodoGoods extends BaseEntity {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Id
    @Column(name = "MALL_CODE")
    private String mallCode;

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

    @Column(name = "DISPLAY_PC_YN")
    private String displayPcYn;

    @Column(name = "SALES_PC_YN")
    private String salesPcYn;

    @Column(name = "DISPLAY_MOBILE_YN")
    private String displayMobileYn;

    @Column(name = "SALES_MOBILE_YN")
    private String salesMobileYn;

    @Column(name = "MALL_STANDARD_PRICE")
    private Integer mallStandardPrice;

    @Column(name = "MALL_COST_VALUE")
    private Integer mallCostValue;

    @Column(name = "EXT1")
    private String ext1;

    @Column(name = "EXT2")
    private String ext2;
}
