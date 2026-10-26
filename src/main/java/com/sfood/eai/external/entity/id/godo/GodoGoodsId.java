package com.sfood.eai.external.entity.id.godo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GodoGoodsId {
    public static final long serialVersionUID = 1L;

    private String companyCode;     // 회사코드
    private String mallCode;        // 연동몰코드
    private String goodsCode;       // 품목코드
}
