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
public class GoodsEdiPartnerId implements Serializable {

    public static final long serialVersionUID = 1L;

    private String companyCode;
    private String mallCode;
    private String partnerGoodsCode;
    private String goodsCode;
}
