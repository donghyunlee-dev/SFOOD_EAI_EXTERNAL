package com.sfood.eai.external.entity.id.goods;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MallGoodsMatchId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "MALL_CODE")
    private String mallCode;

    @Column(name = "MALL_GOODS_CODE")
    private String mallGoodsCode;

    @Column(name = "GOODS_CODE")
    private String goodsCode;

    @Column(name = "OPTION_CODE")
    private String optionCode;
}
