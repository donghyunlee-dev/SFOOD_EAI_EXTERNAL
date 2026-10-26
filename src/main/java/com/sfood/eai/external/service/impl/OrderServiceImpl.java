package com.sfood.eai.external.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.FailDataInfo;
import com.sfood.eai.external.dto.KeyNameValue;
import com.sfood.eai.external.dto.PartnerGoodsPrice;
import com.sfood.eai.external.dto.order.*;
import com.sfood.eai.external.entity.Holiday;
import com.sfood.eai.external.entity.Warehouse;
import com.sfood.eai.external.entity.goods.Goods;
import com.sfood.eai.external.entity.goods.GoodsPrice;
import com.sfood.eai.external.entity.goods.MallGoodsMatch;
import com.sfood.eai.external.entity.goods.SimpleGoods;
import com.sfood.eai.external.entity.id.goods.GoodsId;
import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import com.sfood.eai.external.entity.id.order.OrderMasterId;
import com.sfood.eai.external.entity.id.order.OrderSequenceId;
import com.sfood.eai.external.entity.id.subscription.SubscriptionScheduleMasterId;
import com.sfood.eai.external.entity.order.*;
import com.sfood.eai.external.entity.partner.Partner;
import com.sfood.eai.external.entity.subscription.SubscriptionScheduleMaster;
import com.sfood.eai.external.mapper.order.OrderDeliveryMapper;
import com.sfood.eai.external.mapper.order.OrderGoodsMapper;
import com.sfood.eai.external.mapper.order.OrderGoodsStatusLogMapper;
import com.sfood.eai.external.repository.order.*;
import com.sfood.eai.external.service.*;
import com.sfood.eai.external.service.converter.OrderDataConverter;
import com.sfood.eai.external.service.factory.OrderDataConverterFactory;
import com.sfood.eai.external.type.ChannelType;
import com.sfood.eai.external.type.MallOrderType;
import com.sfood.eai.external.type.OrderStatus;
import com.sfood.eai.external.util.CustomBigDecimalUtils;
import com.sfood.eai.external.util.CustomDateUtils;
import com.sfood.eai.external.util.CustomObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.sfood.eai.external.type.Constants.RETAIL_NH_ORDER_TYPE;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDataConverterFactory orderDataConverterFactory;
    private final PartnerService partnerService;
    private final OrderGoodsService orderGoodsService;
    private final SubscriptionService subscriptionService;
    private final MdmService mdmService;

    private final OrderMasterRepository orderMasterRepository;
    private final OrderDeliveryRepository orderDeliveryRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final OrderGoodsRepository orderGoodsRepository;
    private final OrderClaimRepository orderClaimRepository;
    private final OrderSequenceRepository orderSequenceRepository;
    private final OrderGoodsStatusLogRepository orderGoodsStatusLogRepository;

    @Transactional
    @Override
    public CommonDataRes initOrder(OrderRequest orderRequest) {
        initOrderSequence(orderRequest);
        return CommonDataRes.noDataResponse();
    }

    @Override
    public boolean existsOrder(String companyCode, String marketMallCode, String marketOrderNo) {
        return orderMasterRepository.existsByCompanyCodeAndMarketMallCodeAndMarketOrderNo(companyCode, marketMallCode, marketOrderNo);
    }

    @Override
    public boolean existsOrderGoods(String companyCode, String marketMallCode, String marketOrderNo, String marketOrderSubNo) {
        return orderGoodsRepository.existsByCompanyCodeAndMarketMallCodeAndMarketOrderNoAndMarketOrderSubNo(companyCode, marketMallCode, marketOrderNo, marketOrderSubNo);
    }

    @Override
    public boolean existsOrderClaims(String marketOrderNo, String marketOrderSubNo) {
        return CollectionUtils.isNotEmpty(orderClaimRepository.findOrderClaimsByKey(marketOrderNo, marketOrderSubNo));
    }

    @Async("exExecutor")
    @Transactional
    @Override
    public void saveOrderOne(ChannelType channelType, Object rawOrderData) {
        saveOrder(channelType, rawOrderData);
    }

    @Async("exExecutor")
    @Transactional
    @Override
    public void updateOrderOne(ChannelType channelType, Object rawOrderData) {
        UpdateOrder(channelType, rawOrderData);
    }

    @Async("exExecutor")
    @Transactional
    @Override
    public void saveOrderAll(ChannelType channelType, List rawOrderDataList) {

        for (Object rawOrderData : rawOrderDataList) {
            saveOrder(channelType, rawOrderData);
        }
    }

    @Async("exExecutor")
    @Transactional
    @Override
    public void updateOrderAll(ChannelType channelType, List rawOrderDataList) {

        for (Object rawOrderData : rawOrderDataList) {
            UpdateOrder(channelType, rawOrderData);
        }
    }

    @Async("exExecutor")
    @Transactional
    @Override
    public void saveSubscriptionOrder(ChannelType channelType, Object rawOrderData) {

        List<OrderDataEntity> orderDataList = getSubscriptionOrderByConverter(channelType, rawOrderData);
        for (OrderDataEntity order : orderDataList) {
            order.setOrderNumber(generateOrderMergeNo(order.getCompanyCode(), order.getMarketMallCode(), order.getOrderDate()));

            updateData(order);
            order.validate();
            saveData(order);
        }
    }

    @Async("exExecutor")
    @Transactional
    @Override
    public void updateSubscriptionOrder(ChannelType channelType, Object rawOrderData) {

        OrderDataEntity orderData = getOrderDataByConverter(channelType, rawOrderData);
        List<OrderDataEntity> originalOrders = getOrderDataEntities(orderData.getCompanyCode(), orderData.getMarketMallCode(), orderData.getMarketOrderNo());

        for (OrderDataEntity order : originalOrders) {

            if (!order.isUpdatable()) {
                continue;
            }

            order.getOrderGoods().forEach(goods -> {
                        goods.setPreOrderStatusCode(goods.getOrderStatusCode());
                        goods.setOrderStatusCode(orderData.getOrderGoods().getFirst().getOrderStatusCode());

                        if (goods.isConfirmed()) {
                            goods.confirm();
                        } else {
                            goods.setOrderStatusCode(goods.getPreOrderStatusCode());
                        }
                    }
            );

            saveOrderGoodsStatusLog(order.getOrderGoods());

            order.getOrderDelivery().forEach(delivery ->
                    delivery.update(orderData.getOrderDelivery().getFirst())
            );
        }
    }

    @Async("exExecutor")
    @Transactional
    @Override
    public void saveClaimOrder(ChannelType channelType, OrderStatus status, Object claimOrder) {

        OrderDataEntity claimOrderData = getOrderDataByConverter(channelType, claimOrder);
        OrderDataEntity originOrderData = getOrderDataEntity(claimOrderData.getCompanyCode(), claimOrderData.getMarketMallCode(), claimOrderData.getMarketOrderNo());
        setReturnWarehouse(originOrderData);

        if (ObjectUtils.isEmpty(originOrderData)) {
            return;
        }

        switch (status) {
            case OS6020, OS6040 -> originOrderData.refund(claimOrderData);
            case OS3030 -> originOrderData.cancel(claimOrderData);
            case OS4010, OS4020 -> originOrderData.exchange(claimOrderData);
            case OS5010 -> originOrderData.returned(claimOrderData);
        }

        saveData(originOrderData);
    }

    @Async("exExecutor")
    @Transactional
    @Override
    public void saveSubscriptionClaimOrder(ChannelType channelType, OrderStatus status, Object claimOrder) {

        List<OrderDataEntity> claimOrders = getSubscriptionOrderByConverter(channelType, claimOrder);

        if (CollectionUtils.isEmpty(claimOrders)) {
            return;
        }

        List<OrderDataEntity> originOrderDataList = getOrderDataEntities(claimOrders.getFirst().getCompanyCode(), claimOrders.getFirst().getMarketMallCode(), claimOrders.getFirst().getMarketOrderNo());

        claimOrders.forEach(claim -> {
            originOrderDataList.forEach(originOrder -> {

                    setReturnWarehouse(originOrder);

                    switch (status) {
                            case OS6020, OS6040 -> originOrder.refund(claim);
                            case OS3030 -> originOrder.cancel(claim);
                            case OS4010, OS4020 -> originOrder.exchange(claim);
                            case OS5010, OS5020 -> originOrder.returned(claim);
                        }

                    saveData(originOrder);
                }
            );
        });
    }

    @Async("exExecutor")
    @Transactional
    @Override
    public CompletableFuture<CommonDataRes> resolveErrorOrder(ErrorOrderUpdateReq errorOrderUpdateReq) {
        CommonDataRes res = new CommonDataRes();
        int failedOrderCount = 0;

        List<FailDataInfo> failDataInfoList = new ArrayList<>();
        List<OrderMasterId> errorOrderIds = errorOrderUpdateReq.getErrorOrderList().stream().map(ErrorOrderUpdateReq.ErrorOrderReq::toOrderMasterId).toList();

        for (OrderMasterId id : errorOrderIds) {
            OrderMaster orderMaster = findOrderMasters(id);

            if (ObjectUtils.isEmpty(orderMaster)) {
                continue;
            }

            OrderDataEntity orderDataEntity = OrderDataEntity.builder()
                    .orderMaster(orderMaster)
                    .orderGoods(findOrderGoods(id))
                    .orderDelivery(findOrderDelivery(id))
                    .orderPayment(findOrderPayment(id))
                    .build();

            // 주문 세금계산서 표시 - 파트너 업데이트 시 사용
            if (orderDataEntity.getOrderGoods().stream().anyMatch(goods -> "Y".equals(goods.getReceiptYn()))) {
                orderDataEntity.getOrderMaster().setReceiptFl("t");
            }
            // 거래처 업데이트
            updateOrderPartner(orderDataEntity.getOrderMaster());
            // 정기 배송 일정 업데이트
            updateDeliverySchedule(orderDataEntity);
            // 품목 업데이트
            updateOrderGoodsByInfo(
                    orderDataEntity.getOrderGoods().stream()
                            .filter(goods -> goods.getOrderStatusCode().equals(OrderStatus.OS8010.code())
                                    || goods.getOrderStatusCode().equals(OrderStatus.OS8020.code()))
                            .toList()
            );

            // 예외 오류 마킹
            orderDataEntity.getOrderGoods().stream()
                    .filter(goods -> goods.getOrderStatusCode().equals(OrderStatus.OS8010.code())
                            || goods.getOrderStatusCode().equals(OrderStatus.OS8020.code()))
                    .forEach(goods -> {
                        // 대표품목인 경우, 정기배송이 아니고 상세품목인 경우 (일반 냉동 택배인데 상세품목인 경우)
                        if ("PP".equals(goods.getSubscriptionType()) || (!StringUtils.hasText(goods.getScheduleCode()) && "PD".equals(goods.getSubscriptionType()))) {
                            goods.markAsExceptionError();
                        }
                    });

            boolean isSuccess = orderDataEntity.validate();

            if (!isSuccess) {
                failDataInfoList.add(new FailDataInfo(List.of(new KeyNameValue("ORDER_MERGE_NO", id.getOrderMergeNo())), "주문 업데이트 실패"));
                failedOrderCount++;
            }

            saveData(orderDataEntity);
        }

        res.setSuccessCnt(errorOrderIds.size() - failedOrderCount);
        res.setFailData(failDataInfoList);

        return CompletableFuture.completedFuture(res);
    }

    @Transactional(readOnly = true)
    @Override
    public SimpleOrderGoods getSimpleOrderGoods(OrderGoodsId orderGoodsId) {
        return orderGoodsRepository.findMallOrderGoods(orderGoodsId);
    }

    @Transactional
    @Override
    public void updatePartner(MallOrderId orderId, Partner partner) {
        List<OrderMaster> orderMaster = findOrderMasters(orderId);
        orderMaster.forEach(order -> {
            if (!StringUtils.hasText(order.getOrderPartnerCode()) || order.getOrderPartnerCode().length() < 6) {
                order.updatePartner(partner);
            }
        });
    }

    @Transactional
    @Override
    public void updateGoods(MallOrderGoodsId orderGoodsId, Goods goods, double orderQty) {
        OrderGoods orderGoods = findOrderGoods(orderGoodsId);
        orderGoods.setOrderGoodsCode(goods.getGoodsCode());
        orderGoods.setOrderGoodsName(goods.getGoodsName());
        orderGoods.setOrderCnt(orderQty);
    }

    private void saveOrder(ChannelType channelType, Object rawOrderData) {

        OrderDataEntity orderData = getOrderDataByConverter(channelType, rawOrderData);
        orderData.setOrderNumber(generateOrderMergeNo(orderData.getCompanyCode(), orderData.getMarketMallCode(), orderData.getOrderDate()));

        updateData(orderData);
        orderData.validate();

        if (ChannelType.RETAIL.equals(channelType)) {
            if(!existsOrderGoods(orderData.getCompanyCode(), orderData.getMarketMallCode(), orderData.getMarketOrderNo(), orderData.getOrderGoods().stream().findFirst().get().getMarketOrderSubNo())) {
                saveData(orderData);
            }
        } else {
            saveData(orderData);
        }
    }

    private void UpdateOrder(ChannelType channelType, Object rawOrderData) {

        OrderDataEntity targetOrderData = getOrderDataByConverter(channelType, rawOrderData);

        OrderDataEntity originalOrderData = getOrderDataEntity(
                targetOrderData.getCompanyCode()
                , targetOrderData.getMarketMallCode()
                , targetOrderData.getMarketOrderNo()
        );

        if (ObjectUtils.isEmpty(originalOrderData)) {
            return;
        }

        // 주문 업데이트 가능 상태 여부 체크 - 품목 전체 대상
        // 출고준비중 이후 [출하대기] 부터 업데이트 불가
        if (!originalOrderData.isUpdatable()) {
            return;
        }

        // 원 주문에 수집된 주문 정보 업데이트
        originalOrderData.update(targetOrderData);

        if (!originalOrderData.isChanged()) {
            return;
        }

        // 업데이트된 주문 상세 정보에서 품목 정보 업데이트
        updateOrderGoodsByInfo(originalOrderData.getOrderGoods().stream().filter(goods -> !StringUtils.hasText(goods.getOrderGoodsName())).toList());

        // 주문확정 상태 변경이면 배송일자 업데이트
        if (originalOrderData.getOrderGoods().stream().anyMatch(OrderGoods::isConfirmed)) {
            updateOrderDelivery(originalOrderData);
        }

        // 업데이트된 주문의 데이터 체크
        originalOrderData.validate();

        // 업데이트된 주문 저장
        saveData(originalOrderData);
    }

    private OrderDataEntity getOrderDataByConverter(ChannelType channel, Object rawOrderData) {
        OrderDataConverter dataConverter = orderDataConverterFactory.getConverter(channel);
        return dataConverter.convertOrderEntity(rawOrderData);
    }

    private List<OrderDataEntity> getSubscriptionOrderByConverter(ChannelType channel, Object rawOrderData) {
        OrderDataConverter dataConverter = orderDataConverterFactory.getConverter(channel);
        return dataConverter.convertOrderEntity(rawOrderData);
    }

    private OrderDataEntity getOrderDataEntity(String companyCode, String marketMallCode, String marketOrderNo) {
        try {
            List<OrderMaster> orderMasters = findOrderMasters(new MallOrderId(companyCode, marketMallCode, marketOrderNo));

            if (CollectionUtils.isEmpty(orderMasters)) {
                return null;
            }

            OrderMaster orderMaster = orderMasters.getFirst();
            OrderMasterId orderMasterId = orderMaster.getId();
            List<OrderGoods> orderGoodsList = findOrderGoods(orderMasterId);
            List<OrderDelivery> orderDeliveryList = findOrderDelivery(orderMasterId);
            OrderPayment orderPayment = findOrderPayment(orderMasterId);
            List<OrderClaim> orderClaimList = findOrderClaim(orderMasterId);

            return OrderDataEntity.builder()
                    .orderMaster(orderMaster)
                    .orderGoods(orderGoodsList)
                    .orderDelivery(orderDeliveryList)
                    .orderPayment(orderPayment)
                    .orderClaim(orderClaimList)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private List<OrderDataEntity> getOrderDataEntities(String companyCode, String marketMallCode, String marketOrderNo) {
        try {
            List<OrderMaster> orderMasters = findOrderMasters(new MallOrderId(companyCode, marketMallCode, marketOrderNo));

            if (CollectionUtils.isEmpty(orderMasters)) {
                return null;
            }

            List<OrderDataEntity> orderDataEntities = new ArrayList<>();

            orderMasters.forEach(order -> {
                OrderMasterId orderMasterId = order.getId();
                List<OrderGoods> orderGoodsList = findOrderGoods(orderMasterId);
                List<OrderDelivery> orderDeliveryList = findOrderDelivery(orderMasterId);
                OrderPayment orderPayment = findOrderPayment(orderMasterId);
                List<OrderClaim> orderClaimList = findOrderClaim(orderMasterId);

                orderDataEntities.add(OrderDataEntity.builder()
                        .orderMaster(order)
                        .orderGoods(orderGoodsList)
                        .orderDelivery(orderDeliveryList)
                        .orderPayment(orderPayment)
                        .orderClaim(orderClaimList)
                        .build());
            });

            return orderDataEntities;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private void saveData(OrderDataEntity orderDataEntity) {

        if (ObjectUtils.isEmpty(orderDataEntity)) {
            return;
        }

        if (ObjectUtils.isNotEmpty(orderDataEntity.getOrderMaster())) {

            orderMasterRepository.save(orderDataEntity.getOrderMaster());

            if (CollectionUtils.isNotEmpty(orderDataEntity.getOrderGoods())) {
                orderGoodsRepository.saveAll(orderDataEntity.getOrderGoods());
                saveOrderGoodsStatusLog(orderDataEntity.getOrderGoods());
            }

            if (CollectionUtils.isNotEmpty(orderDataEntity.getOrderDelivery())) {
                orderDeliveryRepository.saveAll(orderDataEntity.getOrderDelivery());
            }

            if (ObjectUtils.isNotEmpty(orderDataEntity.getOrderPayment())) {
                orderPaymentRepository.save(orderDataEntity.getOrderPayment());
            }

            if (CollectionUtils.isNotEmpty(orderDataEntity.getOrderClaim())) {
                orderClaimRepository.saveAll(orderDataEntity.getOrderClaim());
            }
        }
    }

    private void updateData(OrderDataEntity orderDataEntity) {
        // 거래처 & 배송정보 정보 맵핑 (주문 상태에 영향 없게 처리)
        updateOrderPartner(orderDataEntity.getOrderMaster());

        // 품목 업데이트
        updateOrderGoodsByInfo(orderDataEntity.getOrderGoods());

        // 거래처 품목 단가 등록 여부 체크
        // updateExistPartnerGoodsPrice(orderDataEntity.getOrderPartnerCode(), orderDataEntity.getOrderGoods());

        // 품목 업데이트 이후 배송 정보 업데이트 진행
        updateOrderDelivery(orderDataEntity);
    }

    private void updateOrderPartner(OrderMaster orderMaster) {

        Partner partner = null;

        if (MallOrderType.fromByCode(orderMaster.getCompanyCode(), orderMaster.getMarketMallCode()).isOnlineMall()) {
            partner = findOnlineMallPartner(orderMaster);
        } else {
            if (MallOrderType.RETAIL.code().equals(orderMaster.getMarketMallCode())) {
                partner = findRetailPartner(orderMaster);
            }
        }

        if (ObjectUtils.isEmpty(partner)) {
            return;
        }

        orderMaster.updatePartner(partner);
    }

    private void updateOrderGoodsByInfo(List<OrderGoods> orderGoodsList) {

        for (OrderGoods orderGoods : orderGoodsList) {
            try {
                SimpleGoods goods;

                if (MallOrderType.RETAIL.code().equals(orderGoods.getMarketMallCode())) {
                    goods = orderGoodsService.getSimpleGoods(new GoodsId(orderGoods.getCompanyCode(), orderGoods.getOrderGoodsCode()));
                } else {

                    if (!StringUtils.hasText(orderGoods.getOrderGoodsCode())) {
                        List<MallGoodsMatch> mallOrderGoods = orderGoodsService.getMallOrderGoods(orderGoods.getCompanyCode(), orderGoods.getMarketMallCode(), orderGoods.getMarketGoodsCode(), orderGoods.getMarketOptionCode());

                        if (CollectionUtils.isEmpty(mallOrderGoods)) {
                            continue;
                        }

                        goods = orderGoodsService.getGoodsByMall(orderGoods.getCompanyCode(), orderGoods.getMarketMallCode(), mallOrderGoods.getFirst().getId().getGoodsCode());
                    } else {
                        goods = orderGoodsService.getGoodsByMall(orderGoods.getCompanyCode(), orderGoods.getMarketMallCode(), orderGoods.getOrderGoodsCode());
                    }
                }

                if (ObjectUtils.isEmpty(goods)) {
                    continue;
                }

                orderGoods.update(goods);

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private void saveOrderGoodsStatusLog(List<OrderGoods> orderGoodsList) {

        for (OrderGoods orderGoods : orderGoodsList) {
            // 신규이면 무조건 저장
            if (!orderGoods.isStatusChanged() && !orderGoods.isNew()) {
                continue;
            }

            try {
                OrderGoodsStatusLog statusLog = OrderGoodsStatusLogMapper.INSTANCE.toEntity(orderGoods);
                orderGoodsStatusLogRepository.save(statusLog);
            } catch (Exception e) {
                log.error("Failed to save orderGoodsStatusLog : {}", orderGoods, e);
            }
        }
    }

    private void updateOrderDelivery(OrderDataEntity orderDataEntity) {

        if (!MallOrderType.fromByCode(orderDataEntity.getCompanyCode(), orderDataEntity.getMarketMallCode()).isOnlineMall()) {
            return;
        }

        // 정기 배송은 배송일자를 미리 계산함.
        if (OrderDataEntity.OrderType.SUBSCRIPTION.equals(orderDataEntity.getOrderType())) {
            return;
        }

        // 결제 일자 판단 (사방넷, 고도몰 - 입금대기 결제일시 없음)
        LocalDateTime payDateTime = (ObjectUtils.isEmpty(orderDataEntity.getPayDate()) ? orderDataEntity.getOrderDate() : orderDataEntity.getPayDate());
        LocalDateTime orderDateTime = payDateTime.toLocalDate().equals(CustomDateUtils.nowDateTime().toLocalDate())
                ? payDateTime : CustomDateUtils.nowDateTime();

        LocalDateTime closeDateTime = partnerService.getCloseDateTime(orderDataEntity.getCompanyCode(), orderDataEntity.getOrderPartnerCode(), orderDateTime);

        List<Holiday> holidays = CustomObjectUtils.readToFile("static/holidays.json", new TypeReference<List<Holiday>>() {});

        DeliveryDateCalculator deliveryDateCalculator = new DeliveryDateCalculator(holidays);

        LocalDate calculate = deliveryDateCalculator.calculate(orderDateTime, closeDateTime);

        String deliveryDate = CustomDateUtils.formatDateString(calculate);

        // 배송요청일이 필수가 아니라서 공백이 올 수 있고, 한 주문에 다중 배송요청일이 설정될 수 있어서 개별 업데이트 처리
        for (OrderDelivery orderDelivery : orderDataEntity.getOrderDelivery()) {
            try {
                // 배송요청일 지정할 경우 마감시간 판단 없이 설정 (고도몰 명절 선물, 세트 주문 시 배송요청일 지정)
                if (StringUtils.hasText(orderDelivery.getDeliveryReqDate())) {
                    orderDelivery.updateDeliveryDate(orderDelivery.getDeliveryReqDate());
                } else {
                    orderDelivery.updateDeliveryDate(deliveryDate);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                orderDelivery.updateDeliveryDate(deliveryDate);
            }
        }
    }

    private void updatePartnerGoodsPrice(String partnerCode, List<OrderGoods> orderGoodsList) {

        if (CollectionUtils.isEmpty(orderGoodsList)) {
            return;
        }

        for (OrderGoods orderGoods : orderGoodsList) {
            try {

                if (!MallOrderType.fromByCode(orderGoods.getCompanyCode(), orderGoods.getMarketMallCode()).isOnlineMall()) {
                    orderGoods.setMallGoodsCode(orderGoods.getOrderGoodsCode());
                    continue;
                }

                List<PartnerGoodsPrice> pricingList = orderGoodsService.findPartnerGoodsPrices(
                        orderGoods.getCompanyCode()
                        , partnerCode
                        , orderGoods.getOrderGoodsCode());

                if (CollectionUtils.isEmpty(pricingList)) {
                    return;
                }

                orderGoods.setMallGoodsCode(pricingList.getFirst().getGoodsCode());

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private void updateDeliverySchedule(OrderDataEntity orderDataEntity) {

        Optional<OrderGoods> scheduleGoods = orderDataEntity.getOrderGoods().stream()
                .filter(goods -> StringUtils.hasText(goods.getOrderGoodsCode()) && StringUtils.hasText(goods.getScheduleCode()))
                .findFirst();

        if (scheduleGoods.isEmpty()) {
            return;
        }

        List<OrderGoods> orderGoodsList = new ArrayList<>();
        List<OrderDelivery> orderDeliveryList = new ArrayList<>();

        // 배송 스케쥴 만들기
        SubscriptionScheduleMaster scheduleMaster = subscriptionService.getSchedule(new SubscriptionScheduleMasterId(scheduleGoods.get().getCompanyCode(), scheduleGoods.get().getScheduleCode()));

        DeliverySchedule deliverySchedule;

        if (ObjectUtils.isEmpty(scheduleMaster)) {
            deliverySchedule = subscriptionService.getNewDeliverySchedule(orderDataEntity.getCompanyCode(), scheduleGoods.get().getOrderGoodsCode());
            scheduleGoods.get().setScheduleCode(deliverySchedule.getScheduleCode());
        } else {
            // 이미 만들어진 주차, 차수별 주문으로 생성하기 때문에 배송일이 1개이다.
            deliverySchedule = subscriptionService.getNewDeliverySchedule(scheduleMaster);
        }

        if (ObjectUtils.isEmpty(deliverySchedule)) {
            return;
        }

        deliverySchedule.setFirstDeliveryDate(CustomDateUtils.formatDateTimeString(
                orderDataEntity.getOrderDelivery().stream()
                        .filter(del -> del.getOrderDetailSeq().equals(scheduleGoods.get().getOrderDetailSeq()))
                        .map(OrderDelivery::getHopeDelvDate).toList().getFirst()
                , "yyyyMMdd")
        );
        // 배송일에 구성된 품목 코드 리스트 조회
        List<String> goodsCodeList = subscriptionService.findGoodsByScheduleType(deliverySchedule);

        if (CollectionUtils.isEmpty(goodsCodeList)) {
            return;
        }

        List<GoodsPrice> goodsPriceList = goodsCodeList.stream()
                .map(goodsCode -> orderGoodsService.findGoodsPrice(new GoodsId(deliverySchedule.getCompanyCode(), goodsCode)))
                .filter(Objects::nonNull)
                .toList();

        for (GoodsPrice goodsPrice : goodsPriceList) {
            orderDataEntity.getOrderMaster().increaseDetailMaxSequence();
            orderGoodsList.add(OrderGoodsMapper.INSTANCE.toEntity(orderDataEntity.getOrderMaster(), scheduleGoods.get(), goodsPrice, deliverySchedule));
            orderDeliveryList.add(OrderDeliveryMapper.INSTANCE.toEntity(orderDataEntity.getOrderMaster(), orderDataEntity.getOrderDelivery().getFirst()));
        }

        // 정기배송 대표품목 품목 취소 처리
        scheduleGoods.get().setOrderStatusCode("OS3030");
        scheduleGoods.get().setSalesOrderPrice(BigDecimal.ZERO);
        scheduleGoods.get().setSalesSupplyPrice(BigDecimal.ZERO);
        scheduleGoods.get().setSalesVatPrice(BigDecimal.ZERO);
        scheduleGoods.get().setUpdateId("external");

        orderDataEntity.getOrderGoods().addAll(orderGoodsList);
        orderDataEntity.getOrderDelivery().addAll(orderDeliveryList);

        adjustOrderGoodsPrice(orderDataEntity);
    }

    private String generateOrderMergeNo(String companyCode, String mallCode, LocalDateTime collectDate) {
        return orderMasterRepository.getNewOrderMergeNo(companyCode, mallCode, CustomDateUtils.formatDateTimeString(collectDate, "yyyy-MM-dd"));
    }

    private OrderMaster findOrderMasters(OrderMasterId orderMasterId) {
        return orderMasterRepository.findById(orderMasterId).orElse(null);
    }

    private List<OrderMaster> findOrderMasters(MallOrderId mallOrderId) {
        return orderMasterRepository.findByCompanyCodeAndMarketMallCodeAndMarketOrderNo(mallOrderId.getCompanyCode(), mallOrderId.getMarketMallCode(), mallOrderId.getOrderNo());
    }

    private List<OrderGoods> findOrderGoods(OrderMasterId id) {
        return orderGoodsRepository.findByCompanyCodeAndMarketMallCodeAndOrderMergeNo(id.getCompanyCode(), id.getMarketMallCode(), id.getOrderMergeNo());
    }

    private OrderGoods findOrderGoods(MallOrderGoodsId id) {
        return orderGoodsRepository.findByCompanyCodeAndMarketMallCodeAndMarketOrderNoAndMarketOrderSubNo(id.getCompanyCode(), id.getMarketMallCode(), id.getOrderNo(), id.getOrderSubNo());
    }

    private List<OrderDelivery> findOrderDelivery(OrderMasterId id) {
        return orderDeliveryRepository.findByCompanyCodeAndMarketMallCodeAndOrderMergeNo(id.getCompanyCode(), id.getMarketMallCode(), id.getOrderMergeNo());
    }

    private OrderPayment findOrderPayment(OrderMasterId id) {
        return orderPaymentRepository.findByCompanyCodeAndMarketMallCodeAndOrderMergeNo(id.getCompanyCode(), id.getMarketMallCode(), id.getOrderMergeNo()).orElse(null);
    }

    private List<OrderClaim> findOrderClaim(OrderMasterId id) {
        return orderClaimRepository.findByCompanyCodeAndMarketMallCodeAndOrderMergeNo(id.getCompanyCode(), id.getMarketMallCode(), id.getOrderMergeNo());
    }

    private Partner findOnlineMallPartner(OrderMaster orderMaster) {
        return partnerService.getMallPartner(orderMaster.getCompanyCode(), orderMaster.getMarketMallCode(), orderMaster.getInMallCode());
    }

    private Partner findRetailPartner(OrderMaster orderMaster) {

        if (RETAIL_NH_ORDER_TYPE.equals(orderMaster.getInMallCode())) {
            return partnerService.getNhPartner(orderMaster.getCompanyCode());
        } else {
            if (!StringUtils.hasText(orderMaster.getOrderPartnerCode())) {
                return null;
            }
            return partnerService.getPartner(orderMaster.getCompanyCode(), orderMaster.getOrderPartnerCode());
        }
    }

    private void adjustOrderGoodsPrice(OrderDataEntity orderDataEntity) {

        BigDecimal orderPayPrice = orderDataEntity.getOrderPayment().getTotalPayCost();
        // OrderGoods 기준 금액 (OMS 품목 단가 기준) getSalesOrderPrice -> 수량 제외된 단가를 넣어서 수량을 곱하고 합계를 계산해야 한다.
        BigDecimal goodsTotalPrice = orderDataEntity.getOrderGoods().stream()
                .filter(orderGoods -> orderGoods.getSalesOrderPrice() != null)
                .map(orderGoods -> orderGoods.getSalesOrderPrice().multiply(BigDecimal.valueOf(orderGoods.getOrderCnt())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (goodsTotalPrice.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        // 고도몰 금액 비중 = 주문 금액 (품목 금액 + 옵션금액) / OMS 단가를 적용한 품목 전체 합계
        BigDecimal priceRatio = goodsTotalPrice.equals(BigDecimal.ZERO) ? BigDecimal.valueOf(100) : orderPayPrice.divide(goodsTotalPrice, 4, RoundingMode.HALF_UP);

        for (OrderGoods orderGoods : orderDataEntity.getOrderGoods()) {
            orderGoods.setSalesOrderPrice(CustomBigDecimalUtils.multiply(CustomBigDecimalUtils.multiply(orderGoods.getSalesOrderPrice(), BigDecimal.valueOf(orderGoods.getOrderCnt())), priceRatio));
            orderGoods.setSalesUnitPrice(orderGoods.getSalesOrderPrice().divide(BigDecimal.valueOf(orderGoods.getOrderCnt()), 4 , RoundingMode.DOWN));
            // 공급가, 부가세는 품목 정보 업데이트 시 다시 계산
        }
        // priceRatio 로 적용된 품목 금액 합계
        // GodoOrderGoods 금액의 합과 안 맞을 수 있어서 차액을 계산하기 위해 합계를 구한다.
        BigDecimal totalSalesOrderPrice = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getSalesOrderPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 기타 할인 금액 비중으로 적용
        for (OrderGoods orderGoods : orderDataEntity.getOrderGoods()) {
            // 품목 금액 / 배송일 품목 합계 (전체 금액 중 품목이 차지하는 비중 계산)
            BigDecimal goodsRatio = orderGoods.getSalesOrderPrice().divide(totalSalesOrderPrice, 4, RoundingMode.HALF_UP);

            orderGoods.setDcCost(CustomBigDecimalUtils.multiply(orderDataEntity.getOrderPayment().getTotalDcCost(), goodsRatio));
            orderGoods.setGoodsDcCost(CustomBigDecimalUtils.multiply(orderDataEntity.getOrderPayment().getTotalGoodsDcCost(), goodsRatio));
            orderGoods.setMemberDcCost(CustomBigDecimalUtils.multiply(orderDataEntity.getOrderPayment().getTotalMemberDcCost(), goodsRatio));
            orderGoods.setCouponGoodsDcCost(CustomBigDecimalUtils.multiply(orderDataEntity.getOrderPayment().getTotalCouponGoodsDcCost(), goodsRatio));
            orderGoods.setCouponOrderDcCost(CustomBigDecimalUtils.multiply(orderDataEntity.getOrderPayment().getTotalCouponOrderDcCost(), goodsRatio));
            orderGoods.setUseMileage(CustomBigDecimalUtils.multiply(orderDataEntity.getOrderPayment().getTotalUseMileage(), goodsRatio));
            orderGoods.setUseDeposit(CustomBigDecimalUtils.multiply(orderDataEntity.getOrderPayment().getTotalUseDeposit(), goodsRatio));
            orderGoods.setEnuriDcPrice(CustomBigDecimalUtils.multiply(orderDataEntity.getOrderPayment().getTotalEnuriDcPrice(), goodsRatio));
        }

        // 생성된 금액 합계 계산
        BigDecimal totalDcCost = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalGoodsDcCost = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getGoodsDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalMemberDcCost = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getMemberDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCouponGoodsDcPrice = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getCouponGoodsDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCouponOrderDcPrice = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getCouponOrderDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalUseMileage = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getUseMileage).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalUseDeposit = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getUseDeposit).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalEnuriDcPrice = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getEnuriDcPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 차액이 발생했으면 마지막 주문의 마지막 품목에 맞춘다.
        OrderGoods lastGoods = orderDataEntity.getOrderGoods().getLast();
        lastGoods.setSalesOrderPrice(lastGoods.getSalesOrderPrice().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalPayCost(), totalSalesOrderPrice)));
        lastGoods.setSalesUnitPrice(lastGoods.getSalesOrderPrice().divide(BigDecimal.valueOf(lastGoods.getOrderCnt()), 4, RoundingMode.DOWN));
        lastGoods.setDcCost(lastGoods.getDcCost().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalDcCost(), totalDcCost)));
        lastGoods.setGoodsDcCost(lastGoods.getGoodsDcCost().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalGoodsDcCost(), totalGoodsDcCost)));
        lastGoods.setMemberDcCost(lastGoods.getMemberDcCost().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalMemberDcCost(), totalMemberDcCost)));
        lastGoods.setCouponGoodsDcCost(lastGoods.getCouponGoodsDcCost().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalCouponGoodsDcCost(), totalCouponGoodsDcPrice)));
        lastGoods.setCouponOrderDcCost(lastGoods.getCouponOrderDcCost().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalCouponOrderDcCost(), totalCouponOrderDcPrice)));
        lastGoods.setUseMileage(lastGoods.getUseMileage().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalUseMileage(), totalUseMileage)));
        lastGoods.setUseDeposit(lastGoods.getUseDeposit().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalUseDeposit(), totalUseDeposit)));
        lastGoods.setEnuriDcPrice(lastGoods.getEnuriDcPrice().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalEnuriDcPrice(), totalEnuriDcPrice)));
    }

    private void setReturnWarehouse(OrderDataEntity originOrderData) {
        Warehouse returnWarehouse = mdmService.getReturnWarehouse(originOrderData.getCompanyCode());
        originOrderData.setReturnWarehouse(returnWarehouse);
    }

    private void initOrderSequence(OrderRequest orderRequest) {

        LocalDate orderDate = CustomDateUtils.parseLocalDate(orderRequest.getOrderDate());

        if (StringUtils.hasText(orderRequest.getMallCode())) {
            OrderSequenceId orderSeqId = new OrderSequenceId(orderRequest.getCompanyCode(), orderRequest.getMallCode(), orderDate);
            createOrderSequence(orderSeqId);
        } else {
            for (MallOrderType value : MallOrderType.values()) {

                OrderSequenceId orderSeqId = new OrderSequenceId(value.companyCode(), value.code(), orderDate);
                createOrderSequence(orderSeqId);
            }
        }
    }

    private void createOrderSequence(OrderSequenceId id) {
        if (orderSequenceRepository.findById(id).isEmpty()) {
            orderSequenceRepository.save(new OrderSequence(id, 0));
        }
    }
}
