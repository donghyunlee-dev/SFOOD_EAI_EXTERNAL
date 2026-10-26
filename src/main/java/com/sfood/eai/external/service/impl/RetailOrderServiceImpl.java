package com.sfood.eai.external.service.impl;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.order.ErrorOrderUpdateReq;
import com.sfood.eai.external.dto.order.MallOrderGoodsId;
import com.sfood.eai.external.dto.order.MallOrderId;
import com.sfood.eai.external.dto.retail.RetailOrderSearchReq;
import com.sfood.eai.external.entity.goods.Goods;
import com.sfood.eai.external.entity.id.goods.GoodsId;
import com.sfood.eai.external.entity.partner.Partner;
import com.sfood.eai.external.entity.retail.EdiOrderData;
import com.sfood.eai.external.entity.retail.RetailDataEntity;
import com.sfood.eai.external.repository.retail.EdiOrderDataRepository;
import com.sfood.eai.external.service.OrderService;
import com.sfood.eai.external.service.RetailOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.sfood.eai.external.type.Constants.EXTERNAL_USER_ID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RetailOrderServiceImpl implements RetailOrderService {

    private final OrderService orderService;
    private final EdiOrderDataRepository ediOrderDataRepository;
    private final Map<String, String> processID = new ConcurrentHashMap<>();

    @Override
    public List<String> findPartnersInEdiOrders(RetailOrderSearchReq orderSearch) {
        try {
            return ediOrderDataRepository.getRetailPartners(orderSearch.getStartDate(), orderSearch.getEndDate());
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public CommonDataRes saveOrder(RetailOrderSearchReq orderSearch) {

        if (orderSearch.getStartDate().equals(processID.get(orderSearch.getInMallCode()))) {
            return CommonDataRes.noDataResponse();
        }

        try {
            processID.putIfAbsent(orderSearch.getInMallCode(), orderSearch.getStartDate());

            List<EdiOrderData> ediOrderData = ediOrderDataRepository.findByEdiBpCdAndSoDtBetween(orderSearch.getInMallCode(), orderSearch.getStartDate(), orderSearch.getEndDate());

            Map<EdiOrderData.BranchGroupKey, List<EdiOrderData>> orderGroupMap = getEdiOrderGroupMap(ediOrderData);

            for (Map.Entry<EdiOrderData.BranchGroupKey, List<EdiOrderData>> orderGroup : orderGroupMap.entrySet()) {

                RetailDataEntity retailDataEntity = RetailDataEntity.builder()
                        .condition(orderSearch)
                        .orders(orderGroup.getValue())
                        .build();
                // 지점 코드까지 분할해서 주문을 생성하기 때문에 OrderGoods 로 비교해서 등록 여부 판단
                if (!orderService.existsOrderGoods(orderSearch.getCompanyCode(), orderSearch.getMallCode(), orderGroup.getValue().getFirst().getMesgNo(), orderGroup.getValue().getFirst().getLineNo().toString())) {
                    orderService.saveOrderOne(orderSearch.getChannelType(), retailDataEntity);
                }
            }

            return CommonDataRes.getDataResponse(orderGroupMap.size());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            processID.remove(orderSearch.getInMallCode(), orderSearch.getStartDate());
        }

        return CommonDataRes.noDataResponse();
    }

    @Override
    public CommonDataRes updatePartnerAndGoodsOfErrorOrder(ErrorOrderUpdateReq errorOrderUpdateReq) {
        // 주문번호 기준으로 EDI 주문 검색
        List<EdiOrderData> ediErrorOrders = ediOrderDataRepository.findOrderIdsNullBy(errorOrderUpdateReq);
        // EDI 주문 삭제 후 일자별로 프로시저 재실행
        reCreateEdiOrder(ediErrorOrders);
        // 조회한 EDI 주문을 지점별로 그룹 키 생성 - 주문 생성 기준과 동일하게 맞춤
        List<EdiOrderData.BranchGroupKey> branchGroupKey = getBranchGroupKey(ediErrorOrders);

        for (EdiOrderData.BranchGroupKey groupKey : branchGroupKey) {
            try {
                // 오류로 조회한 EDI Order는 삭제했기 때문에 다시 생성한 Edi를 조회한다.
                List<EdiOrderData> orderByPartnerGroupKey = ediOrderDataRepository.findOrderByPartnerGroupKey(groupKey);

                if (CollectionUtils.isEmpty(orderByPartnerGroupKey)) {
                    continue;
                }

                if (StringUtils.hasText(orderByPartnerGroupKey.getFirst().getSoldToParty())) {
                    Partner partner = new Partner(orderByPartnerGroupKey.getFirst().getCompanyCode(), orderByPartnerGroupKey.getFirst().getSoldToParty());
                    orderService.updatePartner(MallOrderId.of(groupKey), partner);
                }

                orderByPartnerGroupKey.stream()
                        .forEach(ediOrder -> {
                            if (StringUtils.hasText(ediOrder.getItemCd())) {
                                MallOrderGoodsId mallOrderGoodsId = MallOrderGoodsId.of(ediOrder);
                                Goods goods = new Goods(new GoodsId(ediOrder.getCompanyCode(), ediOrder.getItemCd()));
                                orderService.updateGoods(mallOrderGoodsId, goods, ediOrder.getSoQty());
                            }
                        });
            } catch (Exception e) {
                log.error("Failed to update for edi order  : {}", groupKey, e);
            }
        }

        CommonDataRes res = new CommonDataRes();
        res.setSuccessCnt(ediErrorOrders.size());
        return res;
    }

    private void reCreateEdiOrder(List<EdiOrderData> errorOrders) {

        if (CollectionUtils.isEmpty(errorOrders)) {
            return;
        }

        ediOrderDataRepository.deleteAllById(errorOrders.stream().map(EdiOrderData::getId).toList());

        String companyCode = errorOrders.getFirst().getCompanyCode();
        List<String> orderDate = errorOrders.stream().map(EdiOrderData::getSoDt).distinct().toList();

        for (String date : orderDate) {
            try {
                String result = ediOrderDataRepository.callProcCreateEdiOrderDataByOrderDate(companyCode, "EDI_ORDER", "EMART", date, EXTERNAL_USER_ID);

                if (result.equals("99")) {
                    log.error("Error occurred while calling the EDI procedure for EMART. Date: {}", date);
                }

                result = ediOrderDataRepository.callProcCreateEdiOrderDataByOrderDate(companyCode, "EDI_ORDER", "EVERYDAY", date, EXTERNAL_USER_ID);

                if (result.equals("99")) {
                    log.error("Error occurred while calling the EDI procedure for EVERYDAY. Date: {}", date);
                }

                result = ediOrderDataRepository.callProcCreateEdiOrderDataByOrderDate(companyCode, "EDI_ORDER", "LOTTEMART", date, EXTERNAL_USER_ID);

                if (result.equals("99")) {
                    log.error("Error occurred while calling the EDI procedure for LOTTEMART. Date: {}", date);
                }

                result = ediOrderDataRepository.callProcCreateEdiOrderDataByOrderDate(companyCode, "EDI_NH_ORDER", "NORDERS", date, EXTERNAL_USER_ID);

                if (result.equals("99")) {
                    log.error("Error occurred while calling the EDI procedure for NORDERS. Date: {}", date);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private Map<EdiOrderData.BranchGroupKey, List<EdiOrderData>> getEdiOrderGroupMap(List<EdiOrderData> orderList) {

        Map<EdiOrderData.BranchGroupKey, List<EdiOrderData>> groupedOrders = new HashMap<>();

        for (EdiOrderData order : orderList) {
            EdiOrderData.BranchGroupKey orderKey = new EdiOrderData.BranchGroupKey(
                    order.getCompanyCode()
                    , order.getEdiType()
                    , order.getMesgFrom()
                    , order.getMesgId()
                    , order.getMesgName()
                    , order.getMesgNo()
                    , order.getMesgTo()
                    , order.getSoDt()
                    , order.getCustBpCd()
            );

            groupedOrders.computeIfAbsent(orderKey, k -> new ArrayList<>()).add(order);
        }

        return groupedOrders;
    }

    private List<EdiOrderData.BranchGroupKey> getBranchGroupKey(List<EdiOrderData> orderIds) {

        Set<EdiOrderData.BranchGroupKey> groupKeys = new HashSet<>();

        orderIds.forEach(order ->
                groupKeys.add(new EdiOrderData.BranchGroupKey(
                        order.getCompanyCode()
                        , order.getEdiType()
                        , order.getMesgFrom()
                        , order.getMesgId()
                        , order.getMesgName()
                        , order.getMesgNo()
                        , order.getMesgTo()
                        , order.getSoDt()
                        , order.getCustBpCd())
                )
        );

        return groupKeys.stream().toList();
    }
}
