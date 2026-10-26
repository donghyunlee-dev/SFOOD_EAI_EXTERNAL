package com.sfood.eai.external.repository.godo;

import com.sfood.eai.external.dto.godo.GodoOrderSearchReq;
import com.sfood.eai.external.dto.order.MallOrderId;

import java.util.List;

public interface GodoOrderRepositoryCustom {

    List<MallOrderId> findUnsettledOrderIds(GodoOrderSearchReq searchReq);
}
