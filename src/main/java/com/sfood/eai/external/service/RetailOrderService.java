package com.sfood.eai.external.service;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.order.ErrorOrderUpdateReq;
import com.sfood.eai.external.dto.retail.RetailOrderSearchReq;

import java.util.List;

public interface RetailOrderService {

    List<String> findPartnersInEdiOrders(RetailOrderSearchReq orderSearch);

    CommonDataRes saveOrder(RetailOrderSearchReq searchReq);

    CommonDataRes updatePartnerAndGoodsOfErrorOrder(ErrorOrderUpdateReq errorOrderUpdateReq);
}
