package com.sfood.eai.external.entity.id.goods;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsOptionId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String companyCode;
    private String goodsCode;
    private int goodsOptionIdx;
}
