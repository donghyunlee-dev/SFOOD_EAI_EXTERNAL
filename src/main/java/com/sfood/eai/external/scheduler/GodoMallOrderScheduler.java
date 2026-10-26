package com.sfood.eai.external.scheduler;

import com.sfood.eai.external.dto.godo.GodoOrderSearchReq;
import com.sfood.eai.external.dto.order.MallOrderId;
import com.sfood.eai.external.service.GodoOrchestratorService;
import com.sfood.eai.external.service.GodoOrderService;
import com.sfood.eai.external.type.InterfaceType;
import com.sfood.eai.external.type.godo.GodoDateType;
import com.sfood.eai.external.type.godo.GodoOrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 고도몰 자동 API 호출 스케쥴러
 * 설명 : 고도몰 API를 정해진 시간마다 호출하기 위하여 스케쥴을 등록한다.
 * 스케쥴 타입 :
 * 1. 주문 수집
 * 2. 클레임 수집
 * <p>
 * 자동 수집 주기
 * 에쓰푸드 - 1차 - 08:00, 2차 - 11:00
 * 존쿡, 메디쏠라 - 10분
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class GodoMallOrderScheduler {

    private final GodoOrderService godoOrderService;
    private final GodoOrchestratorService godoOrchestratorService;

    public void collectOrders(ScheduleProperties properties) {

        GodoOrderSearchReq orderSearchReq = createOrderSearchReq(properties);

        try {
            orderSearchReq.setOrderStatus(GodoOrderStatus.O1.code());
            godoOrchestratorService.processOrder(orderSearchReq);

            Thread.sleep(500);

            orderSearchReq.setOrderStatus(GodoOrderStatus.P1.code());
            godoOrchestratorService.processOrder(orderSearchReq);
         } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void collectOrdersByOrderNo(ScheduleProperties properties) {

        GodoOrderSearchReq orderSearchReq = createOrderSearchReq(properties);

        try {

            List<MallOrderId> mallOrderIds = godoOrderService.retrieveUnSettledOrderIds(orderSearchReq);

            if (CollectionUtils.isEmpty(mallOrderIds)) {
                return;
            }

            for (MallOrderId mallOrderId : mallOrderIds) {
                orderSearchReq.setOrderNo(mallOrderId.getOrderNo());
                godoOrchestratorService.processOrder(orderSearchReq);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void collectClaims(ScheduleProperties properties) {

        GodoOrderSearchReq claimSearchReq = createClaimSearchReq(properties);

        for (GodoOrderStatus status : GodoOrderStatus.values()) {
            try {
                if (status.isClaimStatus()) {
                    claimSearchReq.setDateType(GodoDateType.MODIFY.code());
                    claimSearchReq.setOrderStatus(status.code());
                    godoOrchestratorService.processClaim(claimSearchReq);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private GodoOrderSearchReq createOrderSearchReq(ScheduleProperties properties) {
        GodoOrderSearchReq searchReq = createSearchReq(properties);
        searchReq.setRequestUri(InterfaceType.GODO_ORDER_SEARCH_001.getServiceUri());
        return searchReq;
    }

    private GodoOrderSearchReq createClaimSearchReq(ScheduleProperties properties) {
        GodoOrderSearchReq searchReq = createSearchReq(properties);
        searchReq.setRequestUri(InterfaceType.GODO_CLAIM_SEARCH_001.getServiceUri());
        return searchReq;
    }

    private GodoOrderSearchReq createSearchReq(ScheduleProperties properties) {
        return GodoOrderSearchReq.builder()
                .companyCode(properties.getCompanyCode())
                .userId("scheduler")
                .channelCode(properties.getChannelCode())
                .mallCode(properties.getMallCode())
                .collectDate(properties.getCollectDate())
                .startDate(properties.getCollectDate())
                .endDate(properties.getCollectDate())
                .dateType(GodoDateType.ORDER.code())
                .build();
    }
}
