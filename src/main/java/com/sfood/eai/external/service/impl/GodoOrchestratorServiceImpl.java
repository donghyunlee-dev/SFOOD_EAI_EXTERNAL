package com.sfood.eai.external.service.impl;

import com.sfood.eai.external.config.AppConfig;
import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.CommonSearchReq;
import com.sfood.eai.external.dto.godo.GodoOrderData;
import com.sfood.eai.external.dto.godo.GodoOrderSearchReq;
import com.sfood.eai.external.dto.godo.GodoOrderStatusReq;
import com.sfood.eai.external.dto.godo.GodoOrderStatusRes;
import com.sfood.eai.external.dto.order.OrderInvoiceReq;
import com.sfood.eai.external.dto.order.OrderStatusReq;
import com.sfood.eai.external.entity.godo.GodoDataEntity;
import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import com.sfood.eai.external.service.GodoOrchestratorService;
import com.sfood.eai.external.service.GodoOrderService;
import com.sfood.eai.external.service.OrderService;
import com.sfood.eai.external.type.ChannelType;
import com.sfood.eai.external.type.MallOrderType;
import com.sfood.eai.external.type.OrderStatus;
import com.sfood.eai.external.type.godo.GodoDateType;
import com.sfood.eai.external.type.godo.GodoOrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.batch.support.DatabaseType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static com.sfood.eai.external.entity.godo.GodoDataEntity.GodoOrderType.NORMAL;

@Slf4j
@RequiredArgsConstructor
@Service
public class GodoOrchestratorServiceImpl implements GodoOrchestratorService {

    private final GodoOrderService godoOrderService;
    private final OrderService orderService;

    @SuppressWarnings("unchecked")
    @Override
    public CommonDataRes processOrder(GodoOrderSearchReq searchReq) throws Exception {

        List<GodoOrderData> godoOrders = godoOrderService.retrieveGodoOrders(searchReq);

        if (CollectionUtils.isEmpty(godoOrders)) {
            return CommonDataRes.noDataResponse();
        }

        Map<String, String> orderReceiptFlag = godoOrderService.retrieveOrderReceipts(searchReq);

        for (GodoOrderData godoOrder : godoOrders) {
            try {
                godoOrder.setReceiptFl(orderReceiptFlag.getOrDefault(godoOrder.getOrderNo(), null));

                GodoDataEntity savedGodoData = godoOrderService.saveGodoOrder(searchReq, godoOrder);
                // Godo Data Entity 로 변환 실패 시 다음 주문으로 넘김
                if (ObjectUtils.isEmpty(savedGodoData)) {
                    log.error("고도몰 주문 데이터 변환 실패!!!  =======>> {}", savedGodoData.getOrder().getOrderNo());
                    continue;
                }

                if (!orderService.existsOrder(savedGodoData.getCompanyCode(), savedGodoData.getMallCode(), savedGodoData.getGodoOrderNo())) {
                    // 옵션 여부로 정기배송을 구분한다.
                    // 존쿡도 옵션이 존재할 수 도 있다. 정기배송에서 일반품목으로 처리된다. (사업자, 몰타입으로 구분하면 안된다는 이유로...)
                    if (NORMAL.equals(savedGodoData.getOrderType())) {
                        orderService.saveOrderOne(savedGodoData.getChannelType(), savedGodoData);
                    } else {
                        orderService.saveSubscriptionOrder(savedGodoData.getChannelType(), savedGodoData);
                    }

                } else {
                    if (NORMAL.equals(savedGodoData.getOrderType())) {
                        orderService.updateOrderOne(savedGodoData.getChannelType(), savedGodoData);
                    } else {
                        // 일반 주문 형태로 만들어서 상태와 배송정보만 업데이트 하기 위해 NORMAL로 타입 지정
                        savedGodoData.changeOrderType(NORMAL);
                        orderService.updateSubscriptionOrder(savedGodoData.getChannelType(), savedGodoData);
                    }
                }

            } catch (Exception e) {
                // 로그 출력 후 다음 주문 처리
                log.error("godoMall 주문 저장 오류 : {} - {}", godoOrder.getOrderNo(), e.getMessage());
            }
        }

        return CommonDataRes.getDataResponse(godoOrders.size());
    }

    @Override
    public CommonDataRes processClaim(GodoOrderSearchReq searchReq) throws Exception {

        List<GodoOrderData> godoOrderClaims = godoOrderService.retrieveGodoClaims(searchReq);

        if (CollectionUtils.isEmpty(godoOrderClaims)) {
            return CommonDataRes.noDataResponse();
        }

        OrderStatus status = OrderStatus.valueOf(GodoOrderStatus.getValue(searchReq.getOrderStatus()));

        for (GodoOrderData claimOrder : godoOrderClaims) {
            try {
                GodoDataEntity savedGodoClaimOrder = godoOrderService.saveGodoOrder(searchReq, claimOrder);

                // Godo Data Entity 로 변환 실패 시 다음 주문으로 넘김
                if (ObjectUtils.isEmpty(savedGodoClaimOrder)) {
                    log.error("고도몰 클레임 데이터 변환 실패!!!  =======>> {}", savedGodoClaimOrder.getOrder().getOrderNo());
                    continue;
                }

                // 없는 경우에는 생성할 필요가 없기 때문에 아무것도 진행하지 않는다.
                if (!orderService.existsOrder(savedGodoClaimOrder.getCompanyCode(), savedGodoClaimOrder.getMallCode(), savedGodoClaimOrder.getGodoOrderNo())) {
                    continue;
                }

                if (NORMAL.equals(savedGodoClaimOrder.getOrderType())) {
                    orderService.saveClaimOrder(savedGodoClaimOrder.getChannelType(), status, savedGodoClaimOrder);

                    // 존쿡몰 첫구매 주문 중 신규 회원가입 축하 쿠폰 환불 발생 시 알림.
                    if ("dev".equals(AppConfig.ACTIVE_PROFILE)) {
                        if (MallOrderType.JHONCOOK.code().equals(searchReq.getMallCode()) && "y".equals(savedGodoClaimOrder.getOrder().getFirstSaleFl())) {
                            if (savedGodoClaimOrder.getGoods().stream().anyMatch(goods -> GodoOrderStatus.R3.code().equals(goods.getOrderStatus())
                                    && goods.getDivisionCouponOrderDcPrice().compareTo(BigDecimal.ZERO) > 0)) {
                                log.error("존쿡몰 - 첫구매 주문쿠폰 환불 발생! =======>> {}", savedGodoClaimOrder.getOrder().getOrderNo());
                            }
                        }
                    }
                } else {
                    orderService.saveSubscriptionClaimOrder(savedGodoClaimOrder.getChannelType(), status, savedGodoClaimOrder);
                }

                if (OrderStatus.OS4020.equals(status)) {
                    log.error("교환 주문 발생!!  =======>> {}", savedGodoClaimOrder.getOrder().getOrderNo());
                }

            } catch (Exception e) {
                log.error("[GodoMall] Failed to save claim order : {}", claimOrder.getOrderNo(), e.getMessage());
            }
        }

        return CommonDataRes.getDataResponse(godoOrderClaims.size());
    }

    @Async("exExecutor")
    @Override
    public CompletableFuture<CommonDataRes> processInvoice(OrderInvoiceReq orderInvoiceReq) throws Exception {

        if ("dev".equals(AppConfig.ACTIVE_PROFILE)) {
            return  CompletableFuture.completedFuture(CommonDataRes.noDataResponse());
        }

        CommonDataRes res = new CommonDataRes();

        for (OrderInvoiceReq.MallOrderInvoice invoicesByMall : orderInvoiceReq.getMallInvoiceList()) {

            ChannelType mallChannelType = MallOrderType.fromByCode(orderInvoiceReq.getCompanyCode(), invoicesByMall.getMarketMallCode()).channelType();

            if (!ChannelType.GODOMALL.equals(mallChannelType)) {
                continue;
            }

            orderInvoiceReq.setChannelCode(mallChannelType.code());
            orderInvoiceReq.setMallCode(invoicesByMall.getMarketMallCode());

            GodoOrderStatusRes orderStatusResponse;

            for (OrderInvoiceReq.OrderInvoiceInfo invoice : invoicesByMall.getOrderInvoiceList()) {

                orderStatusResponse = godoOrderService.sendInvoice(orderInvoiceReq, invoice);

                res.addSuccessCnt(orderStatusResponse.getHeader().getCode().equals("000") ? 1 : 0);

                OrderGoodsId orderGoodsId = new OrderGoodsId(
                        orderInvoiceReq.getCompanyCode()
                        , orderInvoiceReq.getMallCode()
                        , invoice.getOrderMergeNo()
                        , invoice.getOrderDetailSeq());

                godoOrderService.saveStatusResponseLog(orderGoodsId, orderStatusResponse);
            }
        }

        return CompletableFuture.completedFuture(res);
    }

    @Async("exExecutor")
    @Override
    public CompletableFuture<CommonDataRes> processOrderStatus(OrderStatusReq mallOrderStatusReq) throws Exception {

        if ("dev".equals(AppConfig.ACTIVE_PROFILE)) {
            return  CompletableFuture.completedFuture(CommonDataRes.noDataResponse());
        }

        CommonDataRes res = new CommonDataRes();

        for (OrderStatusReq.MallOrderStatus mallOrder : mallOrderStatusReq.getMallOrderList()) {

            ChannelType mallChannelType = MallOrderType.fromByCode(mallOrderStatusReq.getCompanyCode(), mallOrder.getMarketMallCode()).channelType();

            if (!ChannelType.GODOMALL.equals(mallChannelType)) {
                continue;
            }

            mallOrderStatusReq.setChannelCode(mallChannelType.code());
            mallOrderStatusReq.setMallCode(mallOrder.getMarketMallCode());
            List<GodoOrderData> collectMallOrder = new ArrayList<>();

            for (OrderStatusReq.OrderStatusInfo orderStatusInfo : mallOrder.getOrderstatusList()) {
                try {

                    GodoOrderStatus orderStatus;

                    try {
                        orderStatus = GodoOrderStatus.fromValue(orderStatusInfo.getOrderStatusCode());
                    } catch (Exception e) {
                        log.warn(e.getMessage());
                        continue;
                    }

                    // 출고준비중으로 전송 시 주문 상태를 확인해서 배송중이면 보내지 않는다.
                    // WMS에서 출고준비중을 2번씩 보내고 송장 전송과 시간대가 겹칠 경우 배송중 -> 출고준비중으로 변경될 수 있어서 체크가 필요함.
                    if (GodoOrderStatus.G1.code().equals(orderStatus.code())) {

                        List<GodoOrderData> godoMallOrders = collectMallOrder.stream().filter(order -> order.getOrderNo().equals(orderStatusInfo.getMarketOrderNo())).toList();

                        if(CollectionUtils.isEmpty(godoMallOrders)) {
                            GodoOrderSearchReq orderSearchReq = new GodoOrderSearchReq();
                            orderSearchReq.copyFrom(mallOrderStatusReq);
                            orderSearchReq.setDateType(GodoDateType.ORDER.code());
                            orderSearchReq.setOrderNo(orderStatusInfo.getMarketOrderNo());
                            orderSearchReq.setOrderStatus(GodoOrderStatus.D1.code());
                            orderSearchReq.setRequestUri("/godo/order-collect");

                            godoMallOrders = godoOrderService.retrieveGodoOrders(orderSearchReq);
                            collectMallOrder.addAll(godoMallOrders);
                        }

                        if (godoMallOrders.size() > 0) {
                            if (godoMallOrders.getFirst().getGodoOrderGoodsList()
                                    .stream()
                                    .anyMatch(godoOrderGoods -> String.valueOf(godoOrderGoods.getSno()).equals(orderStatusInfo.getMarketOrderSubNo()))) {
                                continue;
                            }
                        }
                    }

                    GodoOrderStatusRes orderStatusResponse = godoOrderService.sendOrderStatus(mallOrderStatusReq, orderStatusInfo, orderStatus);
                    res.addSuccessCnt(orderStatusResponse.getHeader().getCode().equals("000") ? 1 : 0);

                    OrderGoodsId orderGoodsId = new OrderGoodsId(
                            mallOrderStatusReq.getCompanyCode()
                            , mallOrder.getMarketMallCode()
                            , orderStatusInfo.getOrderMergeNo()
                            , orderStatusInfo.getOrderDetailSeq()
                    );

                    godoOrderService.saveStatusResponseLog(orderGoodsId, orderStatusResponse);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }

        return CompletableFuture.completedFuture(res);
    }
}
