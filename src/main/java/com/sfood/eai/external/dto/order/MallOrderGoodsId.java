package com.sfood.eai.external.dto.order;

import com.sfood.eai.external.entity.retail.EdiOrderData;
import com.sfood.eai.external.type.MallOrderType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MallOrderGoodsId {

    private String companyCode;
    private String marketMallCode;
    private String orderNo;
    private String orderSubNo;

    public static MallOrderGoodsId of(EdiOrderData ediOrderData) {
        MallOrderGoodsId instance = new MallOrderGoodsId();
        instance.companyCode = ediOrderData.getCompanyCode();
        instance.marketMallCode = MallOrderType.RETAIL.code();
        instance.orderNo = ediOrderData.getMesgNo();
        instance.orderSubNo = String.valueOf(ediOrderData.getLineNo());
        return instance;
    }
}
