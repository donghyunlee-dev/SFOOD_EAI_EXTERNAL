package com.sfood.eai.external.service.impl;

import com.sfood.eai.external.component.document.sabang.data.SabangInvRegister;
import com.sfood.eai.external.config.AppConfig;
import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.order.OrderInvoiceReq;
import com.sfood.eai.external.dto.sabang.SabangInvoiceResult;
import com.sfood.eai.external.dto.sabang.SabangOrderData;
import com.sfood.eai.external.dto.sabang.SabangOrderSearchReq;
import com.sfood.eai.external.entity.sabang.SabangDataEntity;
import com.sfood.eai.external.entity.sabang.SabangInvoiceLog;
import com.sfood.eai.external.entity.sabang.SabangOrder;
import com.sfood.eai.external.mapper.sabang.SabangInvoiceLogMapper;
import com.sfood.eai.external.service.MarketMallService;
import com.sfood.eai.external.service.OrderService;
import com.sfood.eai.external.service.SabangOrchestratorService;
import com.sfood.eai.external.service.SabangOrderService;
import com.sfood.eai.external.type.ChannelType;
import com.sfood.eai.external.type.MallOrderType;
import com.sfood.eai.external.util.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SabangOrchestratorServiceImpl implements SabangOrchestratorService {

    private static final String SABANG_LOTTE_DELIVERY_ID = "002";

    private final SabangOrderService sabangOrderService;
    private final OrderService orderService;
    private final MarketMallService mallService;

    @Override
    public CommonDataRes processOrder(SabangOrderSearchReq orderSearch) throws Exception {

        CommonDataRes res = new CommonDataRes();
        MallConfiguration configuration = mallService.getConfiguration(orderSearch);
        // 파트너별 주문 조회 시 전달할 Document 객체 생성
        List<SabangOrderData> sabangOrderData = sabangOrderService.retrieveSabangOrder(configuration, orderSearch);

        List<SabangOrder> savedOrderData = sabangOrderService.saveSabangOrder(configuration, orderSearch, sabangOrderData);
        // ORDER_ID별로 주문 그룹 생성 -> 동일 ORDER_ID가 1개의 주문
        Map<String, List<SabangOrder>> orderGroups = savedOrderData.stream().collect(Collectors.groupingBy(SabangOrder::getOrderId));

        int successCount = 0;
        // 그룹핑한 1개 주문마다 저장 처리
        for (Map.Entry<String, List<SabangOrder>> order : orderGroups.entrySet()) {

            try {
                // 사방넷 저장 객체 생성
                SabangDataEntity sabangDataEntity = SabangDataEntity.builder()
                        .condition((SabangOrderSearchReq) configuration.getCondition())
                        .orders(order.getValue())
                        .build();

                if (orderService.existsOrder(orderSearch.getCompanyCode(), orderSearch.getMallCode(), order.getValue().getFirst().getOrderId())) {
                    orderService.updateOrderOne(MallOrderType.fromByCode(orderSearch.getCompanyCode(), orderSearch.getMallCode()).channelType(), sabangDataEntity);
                } else {
                    orderService.saveOrderOne(MallOrderType.fromByCode(orderSearch.getCompanyCode(), orderSearch.getMallCode()).channelType(), sabangDataEntity);
                }

                successCount++;
            } catch (Exception e) {
                log.error("사방넷 주문 저장 오류 : {} - {}", order.getValue().getFirst().getOrderId(), e.getMessage());
            }
        }

        res.setSuccessCnt(successCount);

        return res;
    }

    @Override
    public CommonDataRes processInvoice(OrderInvoiceReq orderInvoiceReq) throws Exception {

        if ("dev".equals(AppConfig.ACTIVE_PROFILE)) {
            return CommonDataRes.noDataResponse();
        }

        CommonDataRes res = new CommonDataRes();

        for (OrderInvoiceReq.MallOrderInvoice mallInvoiceList : orderInvoiceReq.getMallInvoiceList()) {

            if (!ChannelType.SABANGNET.equals(MallOrderType.fromByCode(orderInvoiceReq.getCompanyCode(), mallInvoiceList.getMarketMallCode()).channelType())) {
                continue;
            }

            orderInvoiceReq.setMallCode(mallInvoiceList.getMarketMallCode());
            orderInvoiceReq.setChannelCode(ChannelType.SABANGNET.code());

            List<SabangInvRegister.Data> dataModels = new ArrayList<>();
            List<SabangInvoiceLog> invoiceLogs = new ArrayList<>();

            for (OrderInvoiceReq.OrderInvoiceInfo orderInvoiceInfo : mallInvoiceList.getOrderInvoiceList()) {
                // 롯데택배 고정 전송 (계약된 택배사가 롯데만 존재)
                SabangInvRegister.Data dataModel = SabangInvRegister.Data
                        .builder()
                        .sabangnetIdx(orderInvoiceInfo.getMarketOrderSubNo())
                        .takCode(SABANG_LOTTE_DELIVERY_ID)
                        .takInvoice(orderInvoiceInfo.getInvoiceNo())
                        .hopeDelvDate(CustomDateUtils.formatDateTimeString(orderInvoiceInfo.getHopeDelvDate(), "yyyy-MM-dd HH:mm:ss", "yyyyMMdd"))
                        .build();

                dataModels.add(dataModel);
                invoiceLogs.add(SabangInvoiceLogMapper.INSTANCE.toEntity(orderInvoiceReq.getCompanyCode(), orderInvoiceReq.getMallCode(), orderInvoiceInfo, dataModel));
            }

            if (CollectionUtils.isEmpty(dataModels)) {
                res.setSuccessCnt(0);
                return res;
            }

            List<SabangInvoiceResult> invoiceResult = sabangOrderService.sendInvoice(orderInvoiceReq, dataModels);

            sabangOrderService.saveSabangInvoiceLog(invoiceLogs, invoiceResult);

            res.addSuccessCnt((int) invoiceResult.stream().filter(data -> data.getResult().equals("SUCCESS")).count());
        }

        return res;
    }
}
