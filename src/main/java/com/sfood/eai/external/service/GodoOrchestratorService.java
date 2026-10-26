package com.sfood.eai.external.service;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.godo.GodoOrderSearchReq;
import com.sfood.eai.external.dto.order.OrderInvoiceReq;
import com.sfood.eai.external.dto.order.OrderStatusReq;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface GodoOrchestratorService {

    CommonDataRes processOrder(GodoOrderSearchReq searchReq) throws Exception;

    CommonDataRes processClaim(GodoOrderSearchReq searchReq) throws Exception;

    CompletableFuture<CommonDataRes> processInvoice(OrderInvoiceReq orderInvoiceReq) throws Exception;

    CompletableFuture<CommonDataRes> processOrderStatus(OrderStatusReq mallOrderStatus) throws Exception;
}
