package com.sfood.eai.external.service;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.order.OrderInvoiceReq;
import com.sfood.eai.external.dto.sabang.SabangOrderSearchReq;

public interface SabangOrchestratorService {

    CommonDataRes processOrder(SabangOrderSearchReq orderSearch) throws Exception;

    CommonDataRes processInvoice(OrderInvoiceReq orderInvoiceReq) throws Exception;
}
