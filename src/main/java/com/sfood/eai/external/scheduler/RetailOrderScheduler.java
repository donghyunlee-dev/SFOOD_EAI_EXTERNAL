package com.sfood.eai.external.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sfood.eai.external.dto.ApiCallLogReq;
import com.sfood.eai.external.dto.ApiCallLogRes;
import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.retail.RetailOrderSearchReq;
import com.sfood.eai.external.service.MarketMallService;
import com.sfood.eai.external.service.RetailOrderService;
import com.sfood.eai.external.type.OrderStatus;
import com.sfood.eai.external.util.CustomMapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.sfood.eai.external.type.InterfaceType.EDI_ORDER_COLLECT;

/**
 * 리테일 주문 자동 수집 스케쥴러
 * 설명 : DB에 저장되어 있는 리테일 몰마다 주문을 조회하고 OMS 주문으로 변환하는 작업의 일정을 설정한다.
 * 주문 처별 수집 시간
 * 이마트 : 1차 - 10:00, 2차 - 14:20, 3차 - 15:00, 4차 16:10
 * 에브리데이 : 1차 - 13:00, 2차 - 14:50, 3차 - 16:00
 * 롯데슈퍼, 롯데마트 : 1차 : 12:40, 2차 - 13:45, 3차 16:30
 * 농협 : 1차 - 12:00
 * CS유통, 메가마트 : 1차 - 12:00, 2차 - 12:30
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RetailOrderScheduler {

    private final RetailOrderService retailOrderService;
    private final MarketMallService mallService;

    @Transactional
    public void collectOrders(ScheduleProperties properties) {

        try {
            RetailOrderSearchReq searchOrderReq = createSearchReq(properties);
            String logId = UUID.randomUUID().toString();

            saveCollectRequestLog(logId, searchOrderReq);
            CommonDataRes commonDataRes = retailOrderService.saveOrder(searchOrderReq);
            saveCollectResponseLog(logId, commonDataRes);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private RetailOrderSearchReq createSearchReq(ScheduleProperties properties) {
        return RetailOrderSearchReq.builder()
                .companyCode(properties.getCompanyCode())
                .userId(properties.getUserId())
                .mallCode(properties.getMallCode())
                .channelCode(properties.getChannelCode())
                .inMallCode(properties.getInMallCode())
                .mallId(properties.getInMallCode())  // API 로그용
                .collectDate(properties.getCollectDate().replace("-", ""))
                .startDate(properties.getCollectDate().replace("-", ""))
                .endDate(properties.getCollectDate().replace("-", ""))
                .orderStatus(OrderStatus.OS1020.code())
                .build();
    }

    private void saveCollectRequestLog(String logId, RetailOrderSearchReq searchOrderReq) throws JsonProcessingException {
        ApiCallLogReq apiCallLogReq = ApiCallLogReq.builder()
                .id(logId)
                .type(EDI_ORDER_COLLECT)
                .data(CustomMapUtils.extractFields(searchOrderReq))
                .clientIp("")
                .build();

        mallService.saveRequestLog(apiCallLogReq);
    }

    public void saveCollectResponseLog(String logId, CommonDataRes commonDataRes) throws JsonProcessingException {
        ApiCallLogRes apiCallLogRes = ApiCallLogRes.builder()
                .logId(logId)
                .status(200)
                .message("OK")
                .data(commonDataRes)
                .build();

        mallService.saveResponseLog(apiCallLogRes);
    }
}
