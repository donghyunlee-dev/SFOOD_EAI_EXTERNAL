package com.sfood.eai.external.entity.id.sabang;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SabangGoodsId {

    public static final long serialVersionUID = 1L;

    private String companyCode;     // 회사코드
    private String goodsCode;       // 품목코드
}
