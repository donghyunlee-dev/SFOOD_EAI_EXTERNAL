package com.sfood.eai.external.scheduler;

import com.sfood.eai.external.component.http.AppServerManager;
import com.sfood.eai.external.config.AppConfig;
import com.sfood.eai.external.dto.sabang.SabangOrderSearchReq;
import com.sfood.eai.external.entity.partner.MallPartnerInfo;
import com.sfood.eai.external.service.PartnerService;
import com.sfood.eai.external.service.SabangOrchestratorService;
import com.sfood.eai.external.service.SabangOrderService;
import com.sfood.eai.external.type.InterfaceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.sfood.eai.external.type.sabang.SabangOrderStatus.S002;

/**
 * 사방넷 자동 수집 스케쥴러
 * 설명 : 사방넷 API를 정해진 시간마다 호출하기 위하여 스케쥴을 등록한다.
 * 자동 수집 주기 :
 * 1. 8시 20분
 * 2. 8시 30분
 * 3. 15시 20분
 * 4. 15시 20분
 * 2025--03-10 사방넷 클레임 주문 수집을 안하기로 협의 함
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SabangNetOrderScheduler {
    private final SabangOrchestratorService orchestratorService;
    private final PartnerService partnerService;

    public void collectOrders(ScheduleProperties properties) {

        SabangOrderSearchReq orderSearchReq = createOrderSearchReq(properties);

        try {
            List<MallPartnerInfo> mallPartnerList = partnerService.findMallPartnerList(properties.getCompanyCode(), orderSearchReq.getMallCode());

            for (MallPartnerInfo mallPartner : mallPartnerList) {
                orchestratorService.processOrder(orderSearchReq.setMallId(mallPartner.getInMallCode()).setOrderStatus(S002.code()));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private SabangOrderSearchReq createOrderSearchReq(ScheduleProperties properties) {
        return SabangOrderSearchReq.builder()
                .companyCode(properties.getCompanyCode())
                .userId("scheduler")
                .mallCode(properties.getMallCode())
                .channelCode(properties.getChannelCode())
                .requestUri(InterfaceType.SABANG_ORDER_SEARCH_001.getServiceUri())
                .collectDate(properties.getCollectDate())
                .startDate(properties.getCollectDate())
                .endDate(properties.getCollectDate())
                .build();
    }
}
