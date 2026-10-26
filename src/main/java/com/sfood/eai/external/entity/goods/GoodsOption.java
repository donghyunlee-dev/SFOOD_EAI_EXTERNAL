package com.sfood.eai.external.entity.goods;

import com.sfood.eai.external.entity.id.goods.GoodsOptionId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "TBL_GOODS_OPTION")
@IdClass(GoodsOptionId.class)
public class GoodsOption {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Id
    @Column(name = "GOODS_CODE")
    private String goodsCode;

    @Id
    @Column(name = "GOODS_OPTION_IDX")
    private int goodsOptionIdx;

    @Column(name = "OPTION_MST_CODE")
    private String optionMstCode;

    @Column(name = "OPTION_NAME")
    private String optionName;

    @Column(name = "ADD_PRICE")
    private BigDecimal addPrice;

    @Column(name = "RELATION_GOODS_CODE")
    private String relationGoodsCode;

    @Column(name = "SALES_STATUS")
    private String salesStatus;
}
