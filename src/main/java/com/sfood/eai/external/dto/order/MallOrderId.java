package com.sfood.eai.external.dto.order;

import com.sfood.eai.external.entity.id.godo.GodoOrderId;
import com.sfood.eai.external.entity.id.retail.EdiOrderDataId;
import com.sfood.eai.external.entity.id.sabang.SabangOrderId;
import com.sfood.eai.external.entity.retail.EdiOrderData;
import com.sfood.eai.external.type.MallOrderType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MallOrderId {

    private String companyCode;
    private String marketMallCode;
    private String orderNo;

    public static MallOrderId of(GodoOrderId godoOrderId) {
        MallOrderId newInstance = new MallOrderId();
        newInstance.companyCode = godoOrderId.getCompanyCode();
        newInstance.marketMallCode = godoOrderId.getMarketMallCode();
        newInstance.orderNo = godoOrderId.getOrderNo();
        return newInstance;
    }

    public static MallOrderId of(SabangOrderId sabangOrderId) {
        MallOrderId newInstance = new MallOrderId();
        newInstance.companyCode = sabangOrderId.getCompanyCode();
        newInstance.marketMallCode = sabangOrderId.getMarketMallCode();
        newInstance.orderNo = sabangOrderId.getIdx();
        return newInstance;
    }

    public static MallOrderId of(EdiOrderDataId ediOrderDataId) {
        MallOrderId newInstance = new MallOrderId();
        newInstance.companyCode = ediOrderDataId.getCompanyCode();
        newInstance.marketMallCode = MallOrderType.RETAIL.code();
        newInstance.orderNo = ediOrderDataId.getMesgNo();
        return newInstance;
    }

    public static MallOrderId of(EdiOrderData.BranchGroupKey groupKey) {
        MallOrderId newInstance = new MallOrderId();
        newInstance.companyCode = groupKey.companyCode();
        newInstance.marketMallCode = MallOrderType.RETAIL.code();
        newInstance.orderNo = groupKey.mesgNo();

        return newInstance;
    }
}
